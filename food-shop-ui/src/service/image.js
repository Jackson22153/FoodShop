import { ServerURL } from '../constant/FoodShoppingApiURL';
import logo from '../assets/images/logo.png';
import fb from '../assets/images/fb.png';
import twitter from '../assets/images/twitter.png';
import linkedin from '../assets/images/linkedin.png';
import instagram from '../assets/images/instagram.png';

import config from '../../config.json';

import slide1Image from '../assets/images/slides/slide1-img.png';
import slide2Image from '../assets/images/slides/slide2-img.jpg';
import slide3Image from '../assets/images/slides/slide3-img.png';

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

import errorImage from '../assets/images/error.png';

import paypalicon from '../assets/images/paypal.png';
import codicon from '../assets/images/cod.png';

import mobile from '../assets/images/mobile.webp';


export function getMobileImage(){
    return mobile;
}

export function getDefaulImage(){
    return defaultImage;
}
export function getPaypalIcon(){
    return paypalicon;
}
export function getCodIcon(){
    return codicon;
}

const imagePath = '/src/assets/images/'
function image(name){
    return getCurrentUrl() + imagePath + name;
}
function getCurrentUrl(){
    const hostname = window.location.href;
    const pathname = window.location.pathname;
    return hostname.substring(0, hostname.length - pathname.length);
}

// logo
export function getLogo(){
    return image(config.image.logo);
}
// icon
export function getIcon(iconName){
    switch (iconName){
        case 'facebook': return image(config.image.media.facebook);
        case 'twitter': return image(config.image.media.twitter);
        case 'linkedin': return image(config.image.media.linkedin);
        case 'instagram': return image(config.image.media.instagram);
        case 'prev': return image(config.image.navigation.prev);
        case 'next': return image(config.image.navigation.next);
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
    // return slideImage;
    return [slide1Image, slide2Image, slide3Image]
}

function isValidUrl(string) {
    try {
      new URL(string)
      return true;
    } catch (_) {
      return false;  
    }
  }
  


export function displayUserImage(picture){
    if(picture){
        return isValidUrl(picture)?picture: ServerURL+picture;
    }else{
        return defaultUserImage;
    }
}
export function displayProductImage(picture){
    if(picture){
        return isValidUrl(picture)?picture: ServerURL+picture;
    }else{
        return defaultImage;
    }
}