package com.phucx.shop.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.phucx.shop.config.WebConfig;
import com.phucx.shop.model.Category;
import com.phucx.shop.model.CurrentProduct;
import com.phucx.shop.model.ImageFormat;
import com.phucx.shop.model.ProductDetail;
import com.phucx.shop.service.category.CategoryService;
import com.phucx.shop.service.image.ImageService;
import com.phucx.shop.service.product.ProductService;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequestMapping("home")
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ImageService imageService;

    // Category
    @GetMapping("categories")
    public ResponseEntity<Page<Category>> getCategories(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<Category> data = categoryService
            .getCategories(pageNumber, WebConfig.PAGE_SIZE);

        return ResponseEntity.ok().body(data);
    }

    @GetMapping("categories/name/{categoryName}")
    public ResponseEntity<Category> getCategory(
        @PathVariable(name = "categoryName") String categoryName
    ){
        Category data = categoryService.getCategory(categoryName);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("categories/id/{categoryID}")
    public ResponseEntity<Category> getCategoryByID(
        @PathVariable(name = "categoryID") Integer categoryID
    ){
        Category data = categoryService.getCategory(categoryID);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("categories/{categoryName}/products")
    public ResponseEntity<Page<CurrentProduct>> getProductsByCategoryName(
        @PathVariable(name = "categoryName") String categoryName,
        @RequestParam(name = "page", required=false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<CurrentProduct> products = productService
            .getCurrentProductsByCategoryName(categoryName, pageNumber, WebConfig.PAGE_SIZE);

        return ResponseEntity.ok().body(products);
    }


    // products
    @GetMapping("products")
    public ResponseEntity<Page<CurrentProduct>> getProducts(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        var productsPageable =productService.getCurrentProduct(pageNumber, WebConfig.PAGE_SIZE);
        return ResponseEntity.ok().body(productsPageable);
    }
    
    @GetMapping("products/id/{productID}")
    public ResponseEntity<ProductDetail> getProductByID(@PathVariable(name = "productID") Integer productID){
        ProductDetail productDetails = productService.getProductDetail(productID);
        return ResponseEntity.ok().body(productDetails);
    }

    @GetMapping("/products/recommended")
    public ResponseEntity<List<CurrentProduct>> getRecommendedProducts(){
        List<CurrentProduct> products = productService
            .getRecommendedProducts(0, WebConfig.RECOMMENDED_PRODUCT_PAGE_SIZE);
            
        return ResponseEntity.ok().body(products);
    }

    
    // set image
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageFormat> uploadImage(
        @RequestBody MultipartFile file,
        @RequestHeader(name = "X-Forwarded-Uri", required = false) String requestUri,
        @RequestHeader(name = "X-Server-Port", required = false) Integer serverPort
    ) throws IOException {

        String filename = imageService.uploadImage(file);
        String imageUrl = imageService.getCurrentUrl(requestUri, serverPort) + "/" + filename;
        log.info("imageUrl: {}", imageUrl);
        ImageFormat imageFormat = new ImageFormat(imageUrl);
        return ResponseEntity.ok().body(imageFormat);
    }

    @GetMapping("/image/{imagename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imagename) throws IOException {
        byte[] image = imageService.getImage(imagename);
        String mimeType = imageService.getMimeType(imagename);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }
    
   
}
