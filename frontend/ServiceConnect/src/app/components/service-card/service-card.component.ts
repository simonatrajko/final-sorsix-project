import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServiceDTO } from '../../models/ServiceDto';
import { ServiceManagerService } from '../../services/service-manager.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-service-card',
  templateUrl: './service-card.component.html',
  styleUrls: ['./service-card.component.css'],
  imports:[CommonModule,FormsModule]
})
export class ServiceCardComponent {
  @Input({ required: true }) service!: ServiceDTO;
  @Output() deleted:EventEmitter<number> = new EventEmitter
  updateMode=false
  constructor(private serviceManager:ServiceManagerService){

  }
  deleteSelf(){
    this.serviceManager.deleteService(this.service.id).subscribe(res=>{
    this.deleted.emit(this.service.id)
    })
  }

  updatePrice(){
    if(this.updateMode){
      this.updateMode=false
      this.serviceManager.updatePrice(this.service.id,this.service.price).subscribe(res=>console.log(res))
    }
    else{
      this.updateMode=true
    }
  }
}
