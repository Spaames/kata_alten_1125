import { Injectable, signal } from '@angular/core';
import { Product } from 'app/products/data-access/product.model';

export interface CartItem {
  product: Product;
  quantity: number;
}

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly cartItems = signal<CartItem[]>([]);

  get items() {
    return this.cartItems();
  }

  addProduct(product: Product) {
    const items = this.cartItems();
    const index = items.findIndex(item => item.product.id === product.id);
    if (index > -1) {
      // incrementing quantity if product already in cart
      items[index].quantity++;
      this.cartItems.set([...items]);
    } else {
      // otherwise, add it wiht a quantity of 1
      this.cartItems.set([...items, { product, quantity: 1 }]);
    }
  }

  removeProduct(productId: number) {
    const items = this.cartItems();
    const index = items.findIndex(item => item.product.id === productId);
    if (index > -1) {
      if (items[index].quantity > 1) {
        items[index].quantity--;
        this.cartItems.set([...items]);
      } else {
        // remove item
        this.cartItems.set(items.filter(item => item.product.id !== productId));
      }
    }
  }

  deleteProduct(productId: number) {
    const items = this.cartItems();
    this.cartItems.set(items.filter(item => item.product.id !== productId));
  }

  updateQuantity(productId: number, quantity: number) {
    const items = this.cartItems();
    const index = items.findIndex(item => item.product.id === productId);
    if (index > -1) {
      items[index].quantity = quantity;
      this.cartItems.set([...items]);
    }
  }

  clearCart() {
    this.cartItems.set([]);
  }

  getTotalQuantity(): number {
    return this.cartItems().reduce((total, item) => total + item.quantity, 0);
  }
}
