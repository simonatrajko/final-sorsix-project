import { Component } from '@angular/core';
import { Service } from '../../models/Service';
import { ActivatedRoute, Router } from '@angular/router';
import { Validators,FormGroup,FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { ServiceManagerService } from '../../services/service-manager.service';
import { ServiceDTO } from '../../models/ServiceDto';
import { CreateServiceRequest } from '../../models/CreateServiceRequest';
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
    private router:Router,
    private serviceManager:ServiceManagerService

  ) {}

  ngOnInit() {
    this.providerUsername = this.route.snapshot.paramMap.get('username') || '';
    this.serviceForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0)]],
      categoryId: ['', Validators.required],
      duration: [0, [Validators.required, Validators.min(1)]],
    });
  }


  submitService() {
    if (this.serviceForm.invalid) {
      this.serviceForm.markAllAsTouched();
      return;
    }
    const serviceData: CreateServiceRequest = {
      ...this.serviceForm.value
    };
    serviceData.price=serviceData.price.toString()
    serviceData.categoryId=Number(serviceData.categoryId)
    console.log(serviceData)
    this.serviceManager.createService(serviceData).subscribe(res=>{
      console.log("response from backend? " + res)
      this.router.navigateByUrl(`user/${this.providerUsername}`)
    })
  }
}
