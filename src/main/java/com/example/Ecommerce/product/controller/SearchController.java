package com.example.Ecommerce.product.controller;

import com.example.Ecommerce.product.dto.search.ProductDetailDto;
import com.example.Ecommerce.product.dto.search.SearchPageResponse;
import com.example.Ecommerce.product.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

  private final SearchService searchService;

  // 검색어로 검색 - 최신순, 오래된 순
  @GetMapping("/date")
  public ResponseEntity<SearchPageResponse> searchByDate(
      @RequestParam(value = "word") String word,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
      @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort) {

    return ResponseEntity.ok(searchService.searchByDate(word, pageNo, pageSize, sort));
  }

  // 검색어로 검색 - 가격높은순, 가격낮은순
  @GetMapping("/price")
  public ResponseEntity<SearchPageResponse> searchByPrice(
      @RequestParam(value = "word") String word,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
      @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort) {

    return ResponseEntity.ok(searchService.searchByPrice(word, pageNo, pageSize, sort));
  }

  // 검색어로 검색 - 리뷰개수 많은 순, 리뷰개수 적은순
  @GetMapping("/review")
  public ResponseEntity<SearchPageResponse> searchByReview(
      @RequestParam(value = "word") String word,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
      @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort) {

    return ResponseEntity.ok(searchService.searchByReview(word, pageNo, pageSize, sort));
  }

  // 검색어로 검색 - 별점 높은 순, 별점 낮은순
  @GetMapping("/stars")
  public ResponseEntity<SearchPageResponse> searchByStars(
      @RequestParam(value = "word") String word,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
      @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort) {

    return ResponseEntity.ok(searchService.searchByStars(word, pageNo, pageSize, sort));
  }

  // 태그로 검색 - 최신순, 오래된 순
  @GetMapping("/tag/date")
  public ResponseEntity<SearchPageResponse> searchTagByDate(
      @RequestParam(value = "tagName") String tagName,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
      @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort) {

    return ResponseEntity.ok(searchService.searchTagByDate(tagName, pageNo, pageSize, sort));
  }

  // 태그로 검색 - 가격높은순, 가격낮은순
  @GetMapping("/tag/price")
  public ResponseEntity<SearchPageResponse> searchTagByPrice(
      @RequestParam(value = "tagName") String tagName,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
      @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort) {

    return ResponseEntity.ok(searchService.searchTagByPrice(tagName, pageNo, pageSize, sort));
  }

  // 태그로 검색 - 리뷰개수 많은 순, 리뷰개수 적은순
  @GetMapping("/tag/review")
  public ResponseEntity<SearchPageResponse> searchTagByReview(
      @RequestParam(value = "tagName") String tagName,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
      @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort) {

    return ResponseEntity.ok(searchService.searchTagByReview(tagName, pageNo, pageSize, sort));
  }

  // 태그로 검색 - 별점 높은 순, 별점 낮은순
  @GetMapping("/tag/stars")
  public ResponseEntity<SearchPageResponse> searchTagByStars(
      @RequestParam(value = "tagName") String tagName,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
      @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort) {

    return ResponseEntity.ok(searchService.searchTagByStars(tagName, pageNo, pageSize, sort));
  }

  // 카테고리로 검색 - 최신순, 오래된 순
  @GetMapping("/category/date")
  public ResponseEntity<SearchPageResponse> searchCategoryByDate(
      @RequestParam(value = "category") String category,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
      @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort) {

    return ResponseEntity.ok(searchService.searchCategoryByDate(category, pageNo, pageSize, sort));
  }

  // 카테고리로 검색 - 가격높은순, 가격낮은순
  @GetMapping("/category/price")
  public ResponseEntity<SearchPageResponse> searchCategoryByPrice(
      @RequestParam(value = "category") String category,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
      @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort) {

    return ResponseEntity.ok(searchService.searchCategoryByPrice(category, pageNo, pageSize, sort));
  }

  // 카테고리로 검색 - 리뷰개수 많은 순, 리뷰개수 적은순
  @GetMapping("/category/review")
  public ResponseEntity<SearchPageResponse> searchCategoryByReview(
      @RequestParam(value = "category") String category,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
      @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort) {

    return ResponseEntity.ok(
        searchService.searchCategoryByReview(category, pageNo, pageSize, sort));
  }

  // 카테고리로 검색 - 별점 높은 순, 별점 낮은순
  @GetMapping("/category/stars")
  public ResponseEntity<SearchPageResponse> searchCategoryByStars(
      @RequestParam(value = "category") String category,
      @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
      @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort) {

    return ResponseEntity.ok(searchService.searchCategoryByStars(category, pageNo, pageSize, sort));
  }

  // 상품 클릭시 상세정보창 보기
  @GetMapping("/productDetail")
  public ResponseEntity<ProductDetailDto> getProductDetail(@RequestParam Long productId) {

    return ResponseEntity.ok(ProductDetailDto.from(searchService.getProductDetail(productId),
        searchService.getReviewCount(searchService.getProductDetail(productId))));
  }

}
