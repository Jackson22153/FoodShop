package com.phucx.shop.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.shop.config.WebConfig;
import com.phucx.shop.exceptions.EntityExistsException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.ExistedProduct;
import com.phucx.shop.model.ImageFormat;
import com.phucx.shop.model.ProductDetail;
import com.phucx.shop.model.ProductDetails;
import com.phucx.shop.model.ProductSize;
import com.phucx.shop.model.ProductSizeInfo;
import com.phucx.shop.model.ResponseFormat;
import com.phucx.shop.service.image.ProductImageService;
import com.phucx.shop.service.product.ProductService;
import com.phucx.shop.service.product.ProductSizeService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductSizeService productSizeService;

    @Operation(summary = "Update product", tags = {"shop", "post", "admin"})
    @PostMapping
    public ResponseEntity<ResponseFormat> updateProductDetail(
        @RequestBody ProductDetail productDetail
    ) throws NotFoundException{        
        ProductDetail updatedProduct = productService.updateProductDetail(productDetail);
        ResponseFormat data = new ResponseFormat(updatedProduct!=null?true:false);
        return ResponseEntity.ok().body(data);
    }

    @PutMapping
    @Operation(summary = "Add new product", tags = {"shop", "put", "admin"})
    public ResponseEntity<ResponseFormat> insertProductDetail(
        @RequestBody ProductDetail productDetail
    ) throws EntityExistsException{        
        boolean status = productService.insertProductDetail(productDetail);
        ResponseFormat data = new ResponseFormat(status);

        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Get all products", tags = {"shop", "get", "admin"},
        description = "Get all existed products in inventory")
    @GetMapping
    public ResponseEntity<Page<ExistedProduct>> getProducts(
        @RequestParam(name = "page", required = false) Integer page
    ){
        page = page!=null?page:0;
        Page<ExistedProduct> products = productService.getExistedProducts(page, WebConfig.PAGE_SIZE);
        return ResponseEntity.ok().body(products);
    }

    @Operation(summary = "Get product by id", tags = {"shop", "get", "admin"})
    @GetMapping("/{productID}")
    public ResponseEntity<ProductDetails> getProductDetails(
        @PathVariable Integer productID
    ) throws NotFoundException{        
        ProductDetails product = productService.getProductDetails(productID);
        return ResponseEntity.ok().body(product);
    } 

    // set image
    @Operation(summary = "Upload product image", tags = {"shop", "post", "admin"})
    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageFormat> uploadProductImage(
        @RequestBody MultipartFile file,
        @RequestHeader(name = "X-Forwarded-Uri", required = false) String requestUri,
        @RequestHeader(name = "X-Server-Port", required = false) Integer serverPort
    ) throws IOException, NotFoundException {

        String filename = productImageService.uploadProductImage(file);
        String imageUrl = productImageService.getCurrentUrl(requestUri, serverPort) + "/" + filename;
        ImageFormat imageFormat = new ImageFormat(imageUrl);
        return ResponseEntity.ok().body(imageFormat);
    }

    @Operation(summary = "Update product size", tags = {"product", "post", "admin"})
    @PostMapping("/size")
    public ResponseEntity<ResponseFormat> updateProductSize(
        @RequestBody ProductSize productSize
    ) throws NotFoundException{        
        Boolean status = productSizeService.updateProductSize(productSize);
        ResponseFormat data = new ResponseFormat(status);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Update product size infos", tags = {"product", "post", "admin"})
    @PostMapping("/sizes")
    public ResponseEntity<ResponseFormat> getProductSize(
        @RequestBody List<ProductSizeInfo> products
    ) throws NotFoundException{        
        productSizeService.updateProductSizeByProductName(products);
        ResponseFormat data = new ResponseFormat(true);
        return ResponseEntity.ok().body(data);
    }
}
