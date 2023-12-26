package com.example.Ecommerce.product.service;


import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.domain.ProductOption;
import com.example.Ecommerce.product.domain.ProductTag;
import com.example.Ecommerce.product.domain.form.AddProductForm;
import com.example.Ecommerce.product.domain.form.AddProductOptionForm;
import com.example.Ecommerce.product.domain.form.AddProductTagForm;
import com.example.Ecommerce.product.domain.form.CancelProductForm;
import com.example.Ecommerce.product.domain.form.UpdateProductForm;
import com.example.Ecommerce.product.domain.form.UpdateProductOptionForm;
import com.example.Ecommerce.product.domain.form.UpdateProductTagForm;
import com.example.Ecommerce.product.dto.seller.ProductConfirm;
import com.example.Ecommerce.product.dto.seller.ProductListDto;
import com.example.Ecommerce.product.dto.seller.ProductOptionListDto;
import com.example.Ecommerce.product.dto.seller.ProductTagListDto;
import com.example.Ecommerce.product.dto.seller.page.ProductOptionPageResponse;
import com.example.Ecommerce.product.dto.seller.page.ProductPageResponse;
import com.example.Ecommerce.product.dto.seller.page.ProductTagPageResponse;
import com.example.Ecommerce.product.repository.ProductCustomRepository;
import com.example.Ecommerce.product.repository.ProductOptionRepository;
import com.example.Ecommerce.product.repository.ProductRepository;
import com.example.Ecommerce.product.repository.ProductTagRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductCustomRepository productCustomRepository;
  private final ProductOptionRepository productOptionRepository;
  private final ProductTagRepository productTagRepository;

  // 상품 등록 요청
  public Product registerProduct(Long sellerId, AddProductForm form) {
    return productRepository.save(Product.of(sellerId, form));
  }

  // 판매승인된 상품에 옵션 추가
  @Transactional
  public Product addOption(Long sellerId, AddProductOptionForm form) {

    Product product = productRepository.findProductByIdAndSellerId(form.getProductId(), sellerId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    if (!product.getConfirm().equals(ProductConfirm.APPROVED)) {
      throw new CustomException(ErrorCode.PRODUCT_NOT_APPROVED);
    }
    if (product.getProductOptionList().stream()
        .anyMatch(option -> option.getOptionName().equals(form.getOptionName()))) {
      throw new CustomException(ErrorCode.ALREADY_EXIST_OPTION);
    }
    ProductOption productOption = ProductOption.of(sellerId, form);
    product.getProductOptionList().add(productOption);
    return product;
  }

  // 판매승인된 상품에 태그 추가
  @Transactional
  public Product addTag(Long sellerId, AddProductTagForm form) {
    Product product = productRepository.findProductByIdAndSellerId(form.getProductId(), sellerId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    if (!product.getConfirm().equals(ProductConfirm.APPROVED)) {
      throw new CustomException(ErrorCode.PRODUCT_NOT_APPROVED);
    }
    if (product.getProductTags().stream()
        .anyMatch(tag -> tag.getTagName().equals(form.getTagName()))) {
      throw new CustomException(ErrorCode.ALREADY_EXIST_OPTION);
    }
    ProductTag productTag = ProductTag.of(sellerId, form);
    product.getProductTags().add(productTag);
    return product;
  }

  // 등록된 상품 수정하기(옵션 및 태그도 같이 수정가능)
  @Transactional
  public Product updateProduct(Long sellerId, UpdateProductForm form) {

    Product product = productRepository.findProductByIdAndSellerId(form.getId(), sellerId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    if (!product.getConfirm().equals(ProductConfirm.APPROVED)) {
      throw new CustomException(ErrorCode.PRODUCT_NOT_APPROVED);
    }

    product.setName(form.getName());
    product.setPrice(form.getPrice());
    product.setDescription(form.getDescription());
    product.setOrigin(form.getOrigin());
    product.setDiscount(form.getDiscount());

    // 변경할 옵션들
    for (UpdateProductOptionForm updateProductOptionForm : form.getUpdateProductOptionList()) {
      ProductOption productOption = product.getProductOptionList().stream()
          .filter(productOptionItem -> productOptionItem.getId()
              .equals(updateProductOptionForm.getId()))
          .findFirst().orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
      productOption.setOptionName(updateProductOptionForm.getOptionName());
      productOption.setCount(updateProductOptionForm.getCount());
    }

    // 변경할 태그들
    for (UpdateProductTagForm updateProductTagForm : form.getUpdateTagList()) {
      ProductTag productTag = product.getProductTags().stream()
          .filter(productTagItem -> productTagItem.getId().equals(updateProductTagForm.getId()))
          .findFirst().orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_TAG_NOT_FOUND));
      productTag.setTagName(updateProductTagForm.getTagName());
    }

    return product;
  }

  // 등록된 상품의 옵션만 수정하기
  @Transactional
  public ProductOption updateOption(Long sellerId, UpdateProductOptionForm form) {
    ProductOption productOption = productOptionRepository.findById(form.getId())
        .filter(option -> option.getSellerId().equals(sellerId))
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));

    Product product = productRepository.findById(productOption.getProduct().getId())
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    if (!product.getConfirm().equals(ProductConfirm.APPROVED)) {
      throw new CustomException(ErrorCode.PRODUCT_NOT_APPROVED);
    }

    productOption.setOptionName(form.getOptionName());
    productOption.setCount(form.getCount());
    return productOption;
  }

  // 등록된 상품의 태그만 수정하기
  @Transactional
  public ProductTag updateTag(Long sellerId, UpdateProductTagForm form) {
    ProductTag productTag = productTagRepository.findById(form.getId())
        .filter(tag -> tag.getSellerId().equals(sellerId))
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_TAG_NOT_FOUND));

    Product product = productRepository.findById(productTag.getProduct().getId())
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    if (!product.getConfirm().equals(ProductConfirm.APPROVED)) {
      throw new CustomException(ErrorCode.PRODUCT_NOT_APPROVED);
    }

    productTag.setTagName(form.getTagName());
    return productTag;
  }

  // 등록된 상품중 판매중지 혹은 등록 취소된 물품 삭제하기 - 연관된 모든 태그, 옵션들도 같이 삭제됨
  @Transactional
  public void deleteProduct(Long sellerId, Long productId) {
    Product product = productRepository.findProductByIdAndSellerId(productId, sellerId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    if (product.getConfirm().equals(ProductConfirm.APPROVED_CANCEL_SELLING) || product.getConfirm()
        .equals(ProductConfirm.NOT_APPROVED)) {
      productRepository.delete(product);
    } else {
      throw new CustomException(ErrorCode.PRODUCT_NOT_CANCEL_REQUEST);
    }

  }

  // 등록승인된 상품의 옵션 삭제하기
  @Transactional
  public void deleteProductOption(Long sellerId, Long optionId) {
    ProductOption productOption = productOptionRepository.findBySellerIdAndId(sellerId, optionId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
    if (!productOption.getProduct().getConfirm().equals(ProductConfirm.APPROVED)) {
      throw new CustomException(ErrorCode.PRODUCT_NOT_APPROVED);
    }
    productOptionRepository.delete(productOption);

  }

  // 등록승인된 상품의 태그 삭제하기
  @Transactional
  public void deleteProductTag(Long sellerId, Long tagId) {
    ProductTag productTag = productTagRepository.findBySellerIdAndId(sellerId, tagId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_TAG_NOT_FOUND));
    if (!productTag.getProduct().getConfirm().equals(ProductConfirm.APPROVED)) {
      throw new CustomException(ErrorCode.PRODUCT_NOT_APPROVED);
    }
    productTagRepository.delete(productTag);

  }


  // 등록 요청한 상품 취소 요청
  @Transactional
  public String cancelProduct(Long sellerId, CancelProductForm form) {
    Product product = productRepository.findProductByIdAndSellerId(form.getProductId(), sellerId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    if (!product.getConfirm().equals(ProductConfirm.WAITING)) {
      throw new CustomException(ErrorCode.PRODUCT_NOT_WAITING);
    }

    product.setConfirm(ProductConfirm.REQUEST_CANCEL_REGISTER);
    return "등록 취소 요청되었습니다. 관리자 승인후 취소됩니다.";
  }

  // 판매중인 상품 판매 중지 요청
  @Transactional
  public String cancelSellingProduct(Long sellerId, CancelProductForm form) {
    Product product = productRepository.findProductByIdAndSellerId(form.getProductId(), sellerId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    if (!product.getConfirm().equals(ProductConfirm.APPROVED)) {
      throw new CustomException(ErrorCode.PRODUCT_NOT_APPROVED);
    }

    product.setConfirm(ProductConfirm.REQUEST_CANCEL_SELLING);
    return "판매 중지 요청되었습니다. 관리자 승인후 판매 중지됩니다.";
  }


  // 판매 거절된 상품 내역 확인
  public ProductPageResponse getRejectList(Long sellerId, int pageNo, int pageSize) {
    Pageable pageable = PageRequest.of(pageNo, pageSize);
    Page<Product> productPage = productCustomRepository.findByProductConfirm(
        ProductConfirm.NOT_APPROVED, sellerId, pageable);
    List<Product> productList = productPage.getContent();

    return ProductPageResponse.builder()
        .productList(
            productList.stream().map(ProductListDto::from).collect(Collectors.toList()))
        .pageNo(pageNo)
        .pageSize(pageSize)
        .totalElements(productPage.getTotalElements())
        .totalPages(productPage.getTotalPages())
        .last(productPage.isLast())
        .build();
  }

  // 내가 판매하는 상품 리스트 확인
  public ProductPageResponse getSellingList(Long sellerId, int pageNo, int pageSize) {
    Pageable pageable = PageRequest.of(pageNo, pageSize);
    Page<Product> productPage = productCustomRepository.findByProductConfirm(
        ProductConfirm.APPROVED, sellerId, pageable);
    List<Product> productList = productPage.getContent();

    if (productList.isEmpty()) {
      throw new CustomException(ErrorCode.SELLING_PRODUCT_NOT_EXIST);
    }

    return ProductPageResponse.builder()
        .productList(
            productList.stream().map(ProductListDto::getList).collect(Collectors.toList()))
        .pageNo(pageNo)
        .pageSize(pageSize)
        .totalElements(productPage.getTotalElements())
        .totalPages(productPage.getTotalPages())
        .last(productPage.isLast())
        .build();
  }

  // 내가 판매하는 상품 혹은 대기중 이거나 취소된 옵션 리스트 확인
  public ProductOptionPageResponse getOptionList(Long sellerId, Long productId, int pageNo,
      int pageSize) {

    Product product = productRepository.findProductByIdAndSellerId(productId, sellerId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    Pageable pageable = PageRequest.of(pageNo, pageSize);
    Page<ProductOption> productOptionPage = productCustomRepository.findByProductId(
        sellerId, productId, pageable);
    List<ProductOption> productOptionList = productOptionPage.getContent();

    return ProductOptionPageResponse.builder()
        .productOptionList(
            productOptionList.stream()
                .map(productOption -> ProductOptionListDto.from(product, productOption))
                .collect(Collectors.toList()))
        .pageNo(pageNo)
        .pageSize(pageSize)
        .totalElements(productOptionPage.getTotalElements())
        .totalPages(productOptionPage.getTotalPages())
        .last(productOptionPage.isLast())
        .build();
  }

  // 내가 판매하는 상품 옵션 태그 확인
  public ProductTagPageResponse getTagList(Long sellerId, Long productId, int pageNo,
      int pageSize) {

    Product product = productRepository.findProductByIdAndSellerId(productId, sellerId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    Pageable pageable = PageRequest.of(pageNo, pageSize);
    Page<ProductTag> productTagPage = productCustomRepository.findTagByProductId(
        sellerId, productId, pageable);

    List<ProductTag> productTagList = productTagPage.getContent();

    return ProductTagPageResponse.builder()
        .tagList(
            productTagList.stream()
                .map(productTag -> ProductTagListDto.from(product, productTag))
                .collect(Collectors.toList()))
        .pageNo(pageNo)
        .pageSize(pageSize)
        .totalElements(productTagPage.getTotalElements())
        .totalPages(productTagPage.getTotalPages())
        .last(productTagPage.isLast())
        .build();
  }


}
