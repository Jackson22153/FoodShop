package com.phucx.shop.service.product;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.CurrentProduct;
import com.phucx.shop.model.Product;
import com.phucx.shop.model.ProductDetail;
import com.phucx.shop.repository.CurrentProductRepository;
import com.phucx.shop.repository.ProductDetailRepository;
import com.phucx.shop.repository.ProductRepository;
import com.phucx.shop.service.image.ImageService;

import jakarta.persistence.EntityExistsException;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CurrentProductRepository currentProductRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private ImageService imageService;

    @Override
    public List<Product> getProducts() {
        log.info("getProducts()");
        List<Product> products = productRepository.findAll();
        return imageService.setProductsImage(products);
    }

    @Override
    public Page<Product> getProducts(int pageNumber, int pageSize) {
        log.info("getProducts(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        
        Page<Product> productsPageable = productRepository.findAll(page);
        imageService.setProductsImage(productsPageable.getContent());
        return productsPageable;
    }

    @Override
    public Product getProduct(int productID) {
        log.info("getProduct(productID={}", productID);
        Product product = productRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        this.imageService.setProductImage(product);
        return product;
    }

    @Override
    public List<Product> getProducts(String productName) {
        log.info("getProducts(productName={}", productName);
        List<Product> products = productRepository.findByProductName(productName);
        return this.imageService.setProductsImage(products);
    }

    @Override
    public Page<Product> getProductsByName(int pageNumber, int pageSize, String productName) {
        log.info("getProductsByName(productName={}, pageNumber={}, pageSize={}", productName, pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Product> productsPageable = productRepository.findByProductName(productName, page);
        this.imageService.setProductsImage(productsPageable.getContent());
        return productsPageable;
    }

    @Override
    public Page<Product> getProductsByCategoryName(int pageNumber, int pageSize, String categoryName) {
        log.info("getProductsByCategoryName(categoryName={}, pageNumber={}, pageSize={}", categoryName, pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Product> productsPageable = productRepository.findByCategoryNameLike(categoryName, page);
        this.imageService.setProductsImage(productsPageable.getContent());
        return productsPageable;
    }

    @Override
    public List<CurrentProduct> getRecommendedProducts(int pageNumber, int pageSize) {
        log.info("getRecommendedProducts(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProduct> productsPageable = currentProductRepository.findProductsRandom(pageable);
        return this.imageService.setCurrentProductsImage(productsPageable.getContent());
    }

    @Override
    public CurrentProduct getCurrentProduct(int productID) {
        log.info("getCurrentProduct(productID={})", productID);
        CurrentProduct product = currentProductRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        return this.imageService.setCurrentProductImage(product);
    }

    

    @Override
    public List<CurrentProduct> getCurrentProduct() {
        log.info("getCurrentProduct()");
        List<CurrentProduct> products = currentProductRepository.findAll();
        return this.imageService.setCurrentProductsImage(products);
    }

    @Override
    public Page<CurrentProduct> getCurrentProduct(int pageNumber, int pageSize) {
        log.info("getCurrentProduct(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProduct> productsPageable = currentProductRepository.findAll(pageable);
        imageService.setCurrentProductsImage(productsPageable.getContent());
        return productsPageable;
    }

    // search product by name like
    @Override
    public Page<CurrentProduct> searchCurrentProducts(String productName, int pageNumber, int pageSize) {
        log.info("searchCurrentProducts(productName={}, pageNumber={}, pageSize={})", productName, pageNumber, pageSize);
        String searchValue = "%"+productName+"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProduct> productsPageable = currentProductRepository.findByProductNameLike(searchValue, page);
        imageService.setCurrentProductsImage(productsPageable.getContent());
        return productsPageable;
    }

    @Override
    public Page<CurrentProduct> getCurrentProductsByCategoryName(String categoryName, int pageNumber, int pageSize) {
        // replace '-' with "_" for like syntax in sql server
        categoryName = categoryName.replaceAll("-", "_");
        log.info("getCurrentProductsByCategoryName(categoryName={}, pageNumber={}, pageSize={})", categoryName, pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProduct> products = currentProductRepository
            .findByCategoryNameLike(categoryName, page);
        imageService.setCurrentProductsImage(products.getContent());
        return products;
    }

    @Override
    public ProductDetail getProductDetail(int productID) {
        log.info("getProductDetail(productID={})", productID);
        ProductDetail product = productDetailRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        return imageService.setProductDetailImage(product);
    }

    @Override
    public Page<CurrentProduct> getRecommendedProductsByCategory(
        int productID, String categoryName, int pageNumber, int pageSize) {

        log.info("getRecommendedProductsByCategory(productID={}, categoryName={}, pageNumber={}, pageSize={})", 
            productID, categoryName, pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProduct> products = currentProductRepository
            .findRandomByCategoryName(productID, categoryName, page);
        imageService.setCurrentProductsImage(products.getContent());
        return products;
    }

    @Override
    public boolean updateProductDetail(ProductDetail productDetail) {  
        log.info("updateProductDetail()", productDetail.toString());
        if(productDetail.getProductID()==null) throw new NotFoundException("Product Id is null");
        ProductDetail fetchedProduct = productDetailRepository.findById(productDetail.getProductID())
            .orElseThrow(()-> new NotFoundException("Product " + productDetail.getProductID() + " does not found"));
        // extract image's name from url
        String picture = this.imageService.getImageName(productDetail.getPicture());
        // update product detail
        Boolean result = productDetailRepository.updateProduct(
            fetchedProduct.getProductID(), productDetail.getProductName(), 
            productDetail.getQuantityPerUnit(), productDetail.getUnitPrice(), 
            productDetail.getUnitsInStock(), productDetail.getDiscontinued(), 
            picture, productDetail.getDescription(), productDetail.getCategoryID());
        return result;
    }
    @Override
    public boolean insertProductDetail(ProductDetail productDetail) {
        log.info("insertProductDetail({})", productDetail);
        List<Product> products = productRepository.findByProductName(productDetail.getProductName());
        if(!products.isEmpty()){
            throw new EntityExistsException("Product " + productDetail.getProductName() + " already exists");
        }
        // extract image's name from url
        String picture = this.imageService.getImageName(productDetail.getPicture());
        // add new product
        Boolean result = productDetailRepository.insertProduct(
            productDetail.getProductName(), productDetail.getQuantityPerUnit(), 
            productDetail.getUnitPrice(), productDetail.getUnitsInStock(), 
            productDetail.getDiscontinued(), picture, 
            productDetail.getDescription(), productDetail.getCategoryID());
        return result;
    }

    @Override
    public List<Product> getProducts(List<Integer> productIDs) {
        log.info("getProducts(productIds={})", productIDs);
        List<Product> products = productRepository.findAllById(productIDs);
        return this.imageService.setProductsImage(products);
    }

    @Override
    public Product getProduct(Integer productID, Boolean discontinued) {
        log.info("getProduct(productID={}, discontinued={})", productID, discontinued);
        Product product = productRepository.findByProductIDAndDiscontinued(productID, discontinued)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " with discontinued "+ discontinued +" does not found"));
        return this.imageService.setProductImage(product);
    }

    @Override
    public List<CurrentProduct> getCurrentProducts(List<Integer> productIDs) {
        log.info("getCurrentProducts(productIDs={})", productIDs);
        List<CurrentProduct> products = this.currentProductRepository.findAll();
        return this.imageService.setCurrentProductsImage(products);
    }

}
