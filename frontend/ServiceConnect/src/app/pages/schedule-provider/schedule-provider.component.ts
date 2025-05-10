import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ScheduleSlotCardComponent } from '../../components/schedule-slot-card/schedule-slot-card.component';
import { ScheduleSlotFormComponent } from '../../components/schedule-slot-form/schedule-slot-form.component';
import { ScheduleSlotsService } from '../../services/schedule-slots.service';
import { ScheduleSlot } from '../../models/ScheduleSlot';
import { Status } from '../../models/ScheduleSlot';
import { CommonModule } from '@angular/common';
ScheduleSlotFormComponent
@Component({
  selector: 'app-schedule-provider',
  imports: [ScheduleSlotCardComponent,ScheduleSlotFormComponent,CommonModule],
  templateUrl: './schedule-provider.component.html',
  styleUrl: './schedule-provider.component.css'
})
export class ScheduleProviderComponent {
  showForm=false
  username:string|null
  slots:ScheduleSlot[]=[]
  filteredSlots:ScheduleSlot[]=[]
  dayFilter:string|null=null
  avialabilityFilter:string|null=null
  daysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

  constructor(private route:ActivatedRoute,private scheduleSlotService:ScheduleSlotsService){
    this.username= this.route.snapshot.paramMap.get('username');
   
  }
  ngOnInit(){
    this.scheduleSlotService.getAllSlotsByProvider().subscribe(res=>{
      this.slots=res
      this.dayFilter=localStorage.getItem("dayFilter")
      this.avialabilityFilter=localStorage.getItem("avialbilityFilter")
      this.filterSlots()
    })
  }

  handleDeleted(id:number){
    this.slots=this.slots.filter(s=>s.id!=id)
  }

  addSlotForm(){
    this.showForm=true
  }

  removeForm(){
    this.showForm=false
  }

  private filterSlots(){
    if(!this.dayFilter && !this.avialabilityFilter){
      this.filteredSlots=this.slots
    }
    else if(!this.dayFilter && this.avialabilityFilter){
      this.filteredSlots=this.slots.filter(s=>s.status==this.avialabilityFilter)
    }
    else if(this.dayFilter && !this.avialabilityFilter){
      this.filteredSlots=this.slots.filter(s=>s.dayOfWeek==this.dayFilter)
    }
    else{
      this.filteredSlots=this.slots.filter(s=>s.dayOfWeek==this.dayFilter && s.status==this.avialabilityFilter)
    }
  }

  filterSlotsByDay(e:Event){
    let t = e.target as HTMLSelectElement
    let val = t.value.toUpperCase()
    
    this.dayFilter=val
    this.filterSlots()
    localStorage.setItem("dayFilter",val)
  }
  
  filterSlotsByAvilability(e:Event){
    let t = e.target as HTMLSelectElement
    let val = t.value.toUpperCase()
    this.avialabilityFilter=val
    this.filterSlots()
    localStorage.setItem("avialbilityFilter",val)
  }

  addNewSlot(slot:ScheduleSlot){
    slot.status=Status.AVAILABLE
    slot.created_at=new Date().toString()
    this.slots.push(slot)

  }
  
}
