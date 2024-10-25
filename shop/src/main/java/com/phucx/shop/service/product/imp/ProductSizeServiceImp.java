package com.phucx.shop.service.product.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.shop.exceptions.EntityExistsException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.Product;
import com.phucx.shop.model.ProductSize;
import com.phucx.shop.model.ProductSizeInfo;
import com.phucx.shop.repository.ProductRepository;
import com.phucx.shop.repository.ProductSizeInfoRepository;
import com.phucx.shop.repository.ProductSizeRepository;
import com.phucx.shop.service.product.ProductSizeService;
import com.phucx.shop.utils.ImageUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductSizeServiceImp implements ProductSizeService{
    @Autowired
    private ProductSizeRepository productSizeRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductSizeInfoRepository productSizeInfoRepository;

    @Override
    public Boolean createProductSize(ProductSize productSize) throws NotFoundException {
        log.info("createProductSize({})", productSize);
        Product fetchedProduct = productRepository.findById(productSize.getProductID())
            .orElseThrow(
                ()->new NotFoundException("Product " + productSize.getProductID() + " does not found!")
            );
        String id = UUID.randomUUID().toString();
        return productSizeRepository.createProductSize(id, 
            fetchedProduct.getProductID(), productSize.getHeight(), 
            productSize.getWidth(), productSize.getLength(), 
            productSize.getWeight());
    }

    @Override 
    public Boolean createProductSizes(List<ProductSize> productSizes) throws NotFoundException {
        log.info("createProductSizes({})", productSizes);
        return false;
    }

    @Override
    public ProductSize getProductSize(Integer productID) throws NotFoundException {
        log.info("getProductSize(productID={})", productID);
        ProductSize productSize = productSizeRepository.findByProductID(productID).orElseThrow(
            ()-> new NotFoundException("Product size of product " + productID + " does not found!")
        );
        return productSize;
    }

    @Override
    public Boolean updateProductSize(ProductSize productSize) throws NotFoundException {
        log.info("updateProductSize(productSize={})", productSize);
        Product fetchedProduct = productRepository.findById(productSize.getProductID())
            .orElseThrow(
                ()->new NotFoundException("Product " + productSize.getProductID() + " does not found!")
            );
        return productSizeRepository.updateProductSize(
            fetchedProduct.getProductID(), productSize.getHeight(), 
            productSize.getWidth(), productSize.getLength(), 
            productSize.getWeight());
        
    }

    @Override
    public void updateProductSizeByProductName(List<ProductSizeInfo> productSizeInfos) throws NotFoundException {
        log.info("updateProductSizeByProductName({})", productSizeInfos);
        // get product
        List<String> productNames = productSizeInfos.stream()
            .map(ProductSizeInfo::getProductName)
            .collect(Collectors.toList());
        List<ProductSizeInfo> products = productSizeInfoRepository
            .findAllByProductNames(productNames);
        // update new product
        List<ProductSize> productSizes = new ArrayList<>();

        for (ProductSizeInfo productSizeInfo : productSizeInfos) {
            ProductSizeInfo fetchedProduct = products.stream()
                .filter(product -> product.getProductName().equalsIgnoreCase(productSizeInfo.getProductName()))
                .findFirst().orElseThrow(
                    ()->new NotFoundException("Product " + productSizeInfo.getProductName() + " does not found!")
                );  
            productSizes.add(new ProductSize(fetchedProduct.getProductID(), productSizeInfo.getHeight(),
                productSizeInfo.getLength(), productSizeInfo.getWeight(), productSizeInfo.getWidth()));
        }
        // execute update
        for (ProductSize productSize : productSizes) {
            this.updateProductSize(productSize);
        }      
    }

    @Override
    public Boolean createProduct(ProductSizeInfo productSizeInfo) throws EntityExistsException {
        log.info("createProduct({})", productSizeInfo);
        List<Product> products = productRepository.findByProductName(productSizeInfo.getProductName());
        if(!products.isEmpty()){
            throw new EntityExistsException("Product " + productSizeInfo.getProductName() + " already exists");
        }
        // extract image's name from url
        String picture = ImageUtils.getImageName(productSizeInfo.getPicture());
        // add new product
        Boolean result = productSizeInfoRepository.createProduct(
            productSizeInfo.getProductName(), productSizeInfo.getQuantityPerUnit(), 
            productSizeInfo.getUnitPrice(), productSizeInfo.getUnitsInStock(), 
            productSizeInfo.getDiscontinued(), picture, 
            productSizeInfo.getDescription(), productSizeInfo.getCategoryID(),
            productSizeInfo.getHeight(), productSizeInfo.getWidth(),
            productSizeInfo.getLength(), productSizeInfo.getWeight());
        return result;
    }
    
}
