import { Component, Input } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';

 export interface LayoutImage {

  desktop:string;

  mobile:string;

  altText:string;

 }

@Component({

 selector:'app-image-layout',

 standalone:true,

 imports:[CommonModule,NgOptimizedImage],

 templateUrl:'./image-layout.html',

 styleUrls:['./image-layout.css']

})
export class ImageLayout {

 @Input({required:true})

 imagens!:LayoutImage[];

 @Input()

 loading=true;

}