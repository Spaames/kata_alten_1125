import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, ButtonModule],
  templateUrl: './contact.component.html',
})
export class ContactComponent {
  // validating form inputs
  contactForm = new FormBuilder().group({
    email: ['', [Validators.required, Validators.email]],
    message: ['', [Validators.required, Validators.maxLength(300)]],
  });

  submitted = false;
  success = false;

  onSubmit() {
    this.submitted = true;
    if (this.contactForm.valid) {
      this.success = true;
      // api call ? 
      this.contactForm.reset();
      this.submitted = false;
    }
  }
}
