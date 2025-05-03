import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ScheduleSlot } from '../../models/ScheduleSlot';
import { ScheduleSlotCardComponent } from '../../components/schedule-slot-card/schedule-slot-card.component';
import { ScheduleSlotFormComponent } from '../../components/schedule-slot-form/schedule-slot-form.component';
ScheduleSlotFormComponent
@Component({
  selector: 'app-schedule-provider',
  imports: [ScheduleSlotCardComponent,ScheduleSlotFormComponent],
  templateUrl: './schedule-provider.component.html',
  styleUrl: './schedule-provider.component.css'
})
export class ScheduleProviderComponent {
  slots:ScheduleSlot[]=[]
  slotNums:number[]=[]
  private i=0
  private lastId!:number|undefined
  username:string|null
  constructor(private route:ActivatedRoute){
    this.username= this.route.snapshot.paramMap.get('username');
    const stored = localStorage.getItem(`${this.username}slots`);
    this.slots = stored ? JSON.parse(stored) : [];
    this.lastId=this.slots[this.slots.length-1].id
  }
  ngOnInit(){
    
  }

  addSlotForm(){
    this.slotNums.push(this.i)
    this.i++
  }

  ngOnDestroy(){
    localStorage.setItem(`${this.username}slots`,JSON.stringify(this.slots))
  }

  

  handleDeleted(id?:number){
    this.slots=this.slots.filter(s=>s.id!=id)
  }

  handleNewSlot(slot:ScheduleSlot){
    this.lastId!++
    slot.id=this.lastId
    this.slots.push(slot)
  }
}
