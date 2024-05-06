import logo from '../assets/images/logo.png';
import fb from '../assets/images/fb.png';
import twitter from '../assets/images/twitter.png';
import linkedin from '../assets/images/linkedin.png';
import instagram from '../assets/images/instagram.png';
import slideImage from '../assets/images/slide-img.png';
import prevIcon from '../assets/images/prev.png';
import nextIcon from '../assets/images/next.png';

import cardItem1 from '../assets/images/card-item-1.png';
import cardItem2 from '../assets/images/card-item-2.png';
import cardItem3 from '../assets/images/card-item-3.png';

import orange from '../assets/images/orange.png';
import grapes from '../assets/images/grapes.png';
import gauva from '../assets/images/gauva.png';

import tastyFood from '../assets/images/tasty-image.png';

import client from '../assets/images/client.png';

import defaultImage from '../assets/images/defaultImage.png';
import defaultUserImage from '../assets/images/defaultUser.jpg'

import errorImage from '../assets/images/error.png'


export function getDefaulImage(){
    return defaultImage;
}
// logo
export function getLogo(){
    return logo;
}
// icon
export function getIcon(iconName){
    switch (iconName){
        case 'facebook': return fb;
        case 'twitter': return twitter;
        case 'linkedin': return linkedin;
        case 'instagram': return instagram;
        case 'prev': return prevIcon;
        case 'next': return nextIcon;
    }
}
// categories
export function getCardCategory(categoryName){
    switch(categoryName){
        case 'cardItem1': return cardItem1;
        case 'cardItem2': return cardItem2;
        case 'cardItem3': return cardItem3;
    }
}
// error
export function getError(){
    return errorImage;
}
// food 
export function getFood(foodName){
    switch(foodName){
        case 'orange': return orange;
        case 'grapes': return grapes;
        case 'gauva': return gauva;
    }
}
// banner
export function getBanner(){
    return tastyFood;
}
// client
export function getClient(){
    return client;
}
// slide
export function getSlide(){
    return slideImage;
}

export function displayUserImage(picture){
    return picture?picture: defaultUserImage
    // return defaultUserImage
}
export function displayProductImage(picture){
    return picture?picture: defaultImage
}