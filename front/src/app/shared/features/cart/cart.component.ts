import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService } from 'app/shared/data-access/cart.service';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, ButtonModule],
  templateUrl: './cart.component.html'
})
export class CartComponent {
  private readonly cartService = inject(CartService);

  get cartItems() {
    return this.cartService.items;
  }

  // decrease quantity by 1
  remove(productId: number) {
    this.cartService.removeProduct(productId);
  }
  // increase quantity by 1
  add(product: any) {
    this.cartService.addProduct(product);
  }
  // remove product entirely from cart
  delete(productId: number) {
    this.cartService.deleteProduct(productId);
  }
  // clear the entire cart
  clear() {
    this.cartService.clearCart();
  }
  // calculate total price of items in cart
  getTotal(): number {
    return this.cartItems.reduce((total, item) => total + item.product.price * item.quantity, 0);
  }
}
