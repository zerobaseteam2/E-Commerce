package com.example.Ecommerce.product.service;

import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.product.domain.Product;
import com.example.Ecommerce.product.dto.search.SearchDto;
import com.example.Ecommerce.product.dto.search.SearchPageResponse;
import com.example.Ecommerce.product.repository.ProductCustomRepository;
import com.example.Ecommerce.review.repository.ReviewCustomRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final ProductCustomRepository productCustomRepository;
  protected final ReviewCustomRepository reviewCustomRepository;

  // 태그로 검색 - 최신순, 오래된 순
  public SearchPageResponse searchTagByDate(String tagName, int pageNo, int pageSize, String sort) {
    // 날짜별 최신순
    Pageable pageable = PageRequest.of(pageNo, pageSize);
    if (sort.equals("DESC")) {
      Page<Product> productPage = productCustomRepository.findProductsByTagOrderByModifiedAtDesc(
          tagName, pageable);
      return getSearchPageResponse(pageNo, pageSize, productPage);
    }
    // 날짜순 오래된순
    if (sort.equals("ASC")) {
      Page<Product> productPage = productCustomRepository.findProductsByTagOrderByModifiedAtAsc(
          tagName, pageable);
      return getSearchPageResponse(pageNo, pageSize, productPage);
    }
    return null;
  }

  // 태그로 검색 - 가격높은순, 가격낮은순
  public SearchPageResponse searchTagByPrice(String tagName, int pageNo, int pageSize,
      String sort) {
    Pageable pageable = PageRequest.of(pageNo, pageSize);

    // 가격 높은순
    if (sort.equals("DESC")) {
      Page<Product> productPage = productCustomRepository.findProductsByTagOrderByPriceDesc(
          tagName, pageable);
      return getSearchPageResponse(pageNo, pageSize, productPage);
    }

    // 가격 낮은순
    if (sort.equals("ASC")) {
      Page<Product> productPage = productCustomRepository.findProductsByTagOrderByPriceAsc(
          tagName, pageable);
      return getSearchPageResponse(pageNo, pageSize, productPage);
    }

    return null;
  }

  // 태그로 검색 - 리뷰개수 많은 순, 리뷰개수 적은순
  public SearchPageResponse searchTagByReview(String tagName, int pageNo, int pageSize,
      String sort) {
    Pageable pageable = PageRequest.of(pageNo, pageSize);

    // 리뷰 개수 높은순
    if (sort.equals("DESC")) {
      Page<Product> productPage = productCustomRepository.findProductsByTagOrderByReview(
          tagName, pageable);

      if (productPage.isEmpty()) {
        throw new CustomException(ErrorCode.SEARCH_NOT_FOUND_TAG);
      }
      List<Product> products = productPage.getContent();
      return SearchPageResponse.builder()
          .searchDtoList(
              products.stream()
                  .sorted(Comparator.comparing(this::getReviewCount).reversed())
                  .map(product -> SearchDto.from(product, getReviewCount(product)))
                  .collect(Collectors.toList()))
          .pageNo(pageNo)
          .pageSize(pageSize)
          .totalElements(productPage.getTotalElements())
          .totalPages(productPage.getTotalPages())
          .last(productPage.isLast())
          .build();
    }

    // 리뷰 개수 낮은순
    if (sort.equals("ASC")) {
      Page<Product> productPage = productCustomRepository.findProductsByTagOrderByReview(
          tagName, pageable);

      if (productPage.isEmpty()) {
        throw new CustomException(ErrorCode.SEARCH_NOT_FOUND_TAG);
      }
      List<Product> products = productPage.getContent();
      return SearchPageResponse.builder()
          .searchDtoList(
              products.stream()
                  .sorted(Comparator.comparing(this::getReviewCount))
                  .map(product -> SearchDto.from(product, getReviewCount(product)))
                  .collect(Collectors.toList()))
          .pageNo(pageNo)
          .pageSize(pageSize)
          .totalElements(productPage.getTotalElements())
          .totalPages(productPage.getTotalPages())
          .last(productPage.isLast())
          .build();
    }
    return null;
  }

  // 태그로 검색 - 별점 높은 순, 별점 낮은순
  public SearchPageResponse searchTagByStars(String tagName, int pageNo, int pageSize, String sort) {
    Pageable pageable = PageRequest.of(pageNo, pageSize);

    // 가격 높은순
    if (sort.equals("DESC")) {
      Page<Product> productPage = productCustomRepository.findProductsByTagOrderByStarsDesc(
          tagName, pageable);
      return getSearchPageResponse(pageNo, pageSize, productPage);
    }

    // 가격 낮은순
    if (sort.equals("ASC")) {
      Page<Product> productPage = productCustomRepository.findProductsByTagOrderByStarsAsc(
          tagName, pageable);
      return getSearchPageResponse(pageNo, pageSize, productPage);
    }

    return null;
  }

  // 공통함수 - 태그 검색(날짜순)
  private SearchPageResponse getSearchPageResponse(int pageNo, int pageSize,
      Page<Product> productPage) {
    if (productPage.isEmpty()) {
      throw new CustomException(ErrorCode.SEARCH_NOT_FOUND_TAG);
    }

    List<Product> products = productPage.getContent();

    return SearchPageResponse.builder()
        .searchDtoList(
            products.stream()
                .map(product -> SearchDto.from(product, getReviewCount(product)))
                .collect(Collectors.toList()))
        .pageNo(pageNo)
        .pageSize(pageSize)
        .totalElements(productPage.getTotalElements())
        .totalPages(productPage.getTotalPages())
        .last(productPage.isLast())
        .build();
  }

  // 상품별 리뷰 개수 가져오기 함수
  public Long getReviewCount(Product product) {

    return reviewCustomRepository.findByProductId(product.getId());
  }



}
