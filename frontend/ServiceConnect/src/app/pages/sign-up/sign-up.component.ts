import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-sign-up',
  imports: [CommonModule,ReactiveFormsModule,RouterLink],
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.css'
})
export class SignUpComponent {
  
}
