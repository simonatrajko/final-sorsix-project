import { Component } from '@angular/core';
import { Service } from '../../models/Service';
import { ActivatedRoute, Router } from '@angular/router';
import { Validators,FormGroup,FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
@Component({
  selector: 'app-service-form',
  imports: [ReactiveFormsModule,CommonModule],
  templateUrl: './service-form.component.html',
  styleUrl: './service-form.component.css'
})
export class ServiceFormComponent {
  serviceForm!: FormGroup;
  providerUsername: string = '';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private currentUserSerivce:UserService,
    private router:Router
  ) {}

  ngOnInit() {
    this.providerUsername = this.route.snapshot.paramMap.get('username') || '';
    this.serviceForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0)]],
      category: ['', Validators.required],
      createdAt: [this.getTodayDateString()]
    });
  }

  getTodayDateString(): string {
    const today = new Date();
    return today.toISOString().split('T')[0];
  }

  submitService() {
    if (this.serviceForm.invalid) {
      this.serviceForm.markAllAsTouched();
      return;
    }
    const serviceData: Service = {
      ...this.serviceForm.value,
      createdAt: this.serviceForm.value.createdAt ? new Date(this.serviceForm.value.createdAt) : new Date()
    };
    this.currentUserSerivce.handleAddingNewService(serviceData)
    this.router.navigateByUrl(`user/${this.providerUsername}`)
  }
}
