import {
  Component,
  inject,
} from "@angular/core";
import { RouterModule } from "@angular/router";
import { SplitterModule } from 'primeng/splitter';
import { ToolbarModule } from 'primeng/toolbar';
import { PanelMenuComponent } from "./shared/ui/panel-menu/panel-menu.component";
import { CommonModule } from '@angular/common';
import { CartService } from './shared/data-access/cart.service';

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
  standalone: true,
  imports: [RouterModule, SplitterModule, ToolbarModule, PanelMenuComponent, CommonModule],
})
export class AppComponent {
  title = "ALTEN SHOP";
  private readonly cartService = inject(CartService);

  // exposed signal purpose
  public readonly cartItems = this.cartService['cartItems'];

  get cartQuantity(): number {
    return this.cartItems().reduce((total, item) => total + item.quantity, 0);
  }
}