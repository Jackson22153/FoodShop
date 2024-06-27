package com.phucx.shop.service.product;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.exceptions.EntityExistsException;
import com.phucx.shop.exceptions.NotFoundException;
import com.phucx.shop.model.Category;
import com.phucx.shop.model.CurrentProduct;
import com.phucx.shop.model.Product;
import com.phucx.shop.model.ProductDetail;
import com.phucx.shop.model.ProductStockTableType;
import com.phucx.shop.repository.CurrentProductRepository;
import com.phucx.shop.repository.ProductDetailRepository;
import com.phucx.shop.repository.ProductRepository;
import com.phucx.shop.service.category.CategoryService;
import com.phucx.shop.service.image.ImageService;
import com.phucx.shop.service.image.ProductImageService;

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
    private CategoryService categoryService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ProductImageService productImageService;

    @Override
    public List<Product> getProducts() {
        log.info("getProducts()");
        List<Product> products = productRepository.findAll();
        return productImageService.setProductsImage(products);
    }

    @Override
    public Page<Product> getProducts(int pageNumber, int pageSize) {
        log.info("getProducts(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        
        Page<Product> productsPageable = productRepository.findAll(page);
        productImageService.setProductsImage(productsPageable.getContent());
        return productsPageable;
    }

    @Override
    public Product getProduct(int productID) throws NotFoundException {
        log.info("getProduct(productID={}", productID);
        Product product = productRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        this.productImageService.setProductImage(product);
        return product;
    }

    @Override
    public List<Product> getProducts(String productName) {
        log.info("getProducts(productName={}", productName);
        List<Product> products = productRepository.findByProductName(productName);
        return this.productImageService.setProductsImage(products);
    }

    @Override
    public Page<Product> getProductsByName(int pageNumber, int pageSize, String productName) {
        log.info("getProductsByName(productName={}, pageNumber={}, pageSize={}", productName, pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Product> productsPageable = productRepository.findByProductName(productName, page);
        this.productImageService.setProductsImage(productsPageable.getContent());
        return productsPageable;
    }

    @Override
    public Page<Product> getProductsByCategoryName(int pageNumber, int pageSize, String categoryName) {
        log.info("getProductsByCategoryName(categoryName={}, pageNumber={}, pageSize={}", categoryName, pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Product> productsPageable = productRepository.findByCategoryNameLike(categoryName, page);
        this.productImageService.setProductsImage(productsPageable.getContent());
        return productsPageable;
    }

    @Override
    public List<CurrentProduct> getRecommendedProducts(int pageNumber, int pageSize) {
        log.info("getRecommendedProducts(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProduct> productsPageable = currentProductRepository.findProductsRandom(pageable);
        return this.productImageService.setCurrentProductsImage(productsPageable.getContent());
    }

    @Override
    public CurrentProduct getCurrentProduct(int productID) throws NotFoundException {
        log.info("getCurrentProduct(productID={})", productID);
        CurrentProduct product = currentProductRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        return this.productImageService.setCurrentProductImage(product);
    }

    

    @Override
    public List<CurrentProduct> getCurrentProduct() {
        log.info("getCurrentProduct()");
        List<CurrentProduct> products = currentProductRepository.findAll();
        return this.productImageService.setCurrentProductsImage(products);
    }

    @Override
    public Page<CurrentProduct> getCurrentProduct(int pageNumber, int pageSize) {
        log.info("getCurrentProduct(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProduct> productsPageable = currentProductRepository.findAll(pageable);
        productImageService.setCurrentProductsImage(productsPageable.getContent());
        return productsPageable;
    }

    // search product by name like
    @Override
    public Page<CurrentProduct> searchCurrentProducts(String productName, int pageNumber, int pageSize) {
        log.info("searchCurrentProducts(productName={}, pageNumber={}, pageSize={})", productName, pageNumber, pageSize);
        String searchValue = "%"+productName+"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProduct> productsPageable = currentProductRepository.searchCurrentProductsByProductName(searchValue, page);
        log.info("SearchProducts: {}", productsPageable.getContent());
        productImageService.setCurrentProductsImage(productsPageable.getContent());
        return productsPageable;
    }

    @Override
    public Page<CurrentProduct> getCurrentProductsByCategoryName(String categoryName, int pageNumber, int pageSize) throws NotFoundException {
        // replace '-' with "_" for like syntax in sql server
        categoryName = categoryName.replaceAll("-", "_");
        log.info("getCurrentProductsByCategoryName(categoryName={}, pageNumber={}, pageSize={})", categoryName, pageNumber, pageSize);
        // get category
        List<Category> categories = categoryService.getCategoryLike(categoryName);
        if(categories==null || categories.isEmpty()) 
            throw new NotFoundException("Category " + categoryName + " does not found");
        Category fetchedCategory = categories.get(0);
        // get products based on category name
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProduct> products = currentProductRepository
            .findByCategoryName(fetchedCategory.getCategoryName(), page);
        productImageService.setCurrentProductsImage(products.getContent());
        return products;
    }

    @Override
    public ProductDetail getProductDetail(int productID) throws NotFoundException {
        log.info("getProductDetail(productID={})", productID);
        ProductDetail product = productDetailRepository.findById(productID)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " does not found"));
        return productImageService.setProductDetailImage(product);
    }

    @Override
    public Page<CurrentProduct> getRecommendedProductsByCategory(
        int productID, String categoryName, int pageNumber, int pageSize) {
        
        log.info("getRecommendedProductsByCategory(productID={}, categoryName={}, pageNumber={}, pageSize={})", 
            productID, categoryName, pageNumber, pageSize);
        // replace '-' with "_" for like syntax in sql server
        categoryName = categoryName.replaceAll("-", "_");
            
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProduct> products = currentProductRepository
            .findRandomLikeCategoryNameWithoutProductID(productID, categoryName, page);
        productImageService.setCurrentProductsImage(products.getContent());
        return products;
    }

    @Override
    public boolean updateProductDetail(ProductDetail productDetail) throws NotFoundException {  
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
    public boolean insertProductDetail(ProductDetail productDetail) throws EntityExistsException {
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
        this.productImageService.setProductsImage(products);
        return products;
    }

    @Override
    public Product getProduct(Integer productID, Boolean discontinued) throws NotFoundException {
        log.info("getProduct(productID={}, discontinued={})", productID, discontinued);
        Product product = productRepository.findByProductIDAndDiscontinued(productID, discontinued)
            .orElseThrow(()-> new NotFoundException("Product " + productID + " with discontinued "+ discontinued +" does not found"));
        return this.productImageService.setProductImage(product);
    }

    @Override
    public List<CurrentProduct> getCurrentProducts(List<Integer> productIDs) {
        log.info("getCurrentProducts(productIDs={})", productIDs);
        List<CurrentProduct> products = this.currentProductRepository.findAllById(productIDs);
        this.productImageService.setCurrentProductsImage(products);
        return products;
    }

    @Override
    public Boolean updateProductsInStock(List<ProductStockTableType> productStocks) {
        log.info("updateProductsInStock(productStocks={})", productStocks);
        List<String> productIDs = new ArrayList<>();
        List<String> unitsInStocks = new ArrayList<>();
        for (ProductStockTableType productStock : productStocks) {
            productIDs.add(String.valueOf(productStock.getProductID()));
            unitsInStocks.add(String.valueOf(productStock.getUnitsInStock()));
        }
        String productIDsStr = String.join(",", productIDs);
        String unitsInStocksStr = String.join(",", unitsInStocks);
        
        Boolean status = productRepository.updateProductsUnitsInStock(productIDsStr, unitsInStocksStr);
        return status;
    }

}
