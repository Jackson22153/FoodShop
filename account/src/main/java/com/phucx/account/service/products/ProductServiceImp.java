package com.phucx.account.service.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.account.model.ProductDetails;
import com.phucx.account.repository.ProductDetailsRepository;
import com.phucx.account.service.github.GithubService;

@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    @Autowired
    private GithubService githubService;

    @Override
    public boolean updateProductDetails(ProductDetails productDetails) {  
        try {
            String imageUrl = null;
            var opFood = productDetailsRepository
                .findById(productDetails.getProductID());
            if(opFood.isPresent()){
                String picture = productDetails.getPicture();
                ProductDetails fetchedFood = opFood.get();
                if(picture!=null){
                    if(fetchedFood.getPicture()==null){
                        imageUrl = githubService.uploadImage(picture);
                    }else{
                        int comparedPicture =fetchedFood.getPicture().compareToIgnoreCase(picture);
                        if(comparedPicture!=0){
                            imageUrl = githubService.uploadImage(picture);
                        }else if(comparedPicture==0){
                            imageUrl = fetchedFood.getPicture();
                        }
                    }
                }
                productDetailsRepository.updateProduct(
                    productDetails.getProductID(), productDetails.getProductName(), 
                    productDetails.getQuantityPerUnit(), productDetails.getUnitPrice(), 
                    productDetails.getUnitsInStock(), productDetails.getUnitsOnOrder(), 
                    productDetails.getReorderLevel(), productDetails.getDiscontinued(), 
                    imageUrl, productDetails.getCategoryID(), 
                    productDetails.getSupplierID());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean insertProductDetails(ProductDetails productDetails) {
        String imageUrl = null;
        try {
            String picture = productDetails.getPicture();
            if(picture!=null){
                imageUrl = githubService.uploadImage(picture);
            }
            productDetailsRepository.insertProduct(
                productDetails.getProductName(), productDetails.getQuantityPerUnit(), 
                productDetails.getUnitPrice(), productDetails.getUnitsInStock(), 
                productDetails.getUnitsOnOrder(), productDetails.getReorderLevel(), 
                productDetails.getDiscontinued(), imageUrl, 
                productDetails.getCategoryID(), 
                productDetails.getSupplierID());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
