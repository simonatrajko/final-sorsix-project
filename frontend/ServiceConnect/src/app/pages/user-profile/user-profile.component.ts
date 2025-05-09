import { Component } from '@angular/core';
import { Router,RouterLink } from '@angular/router';
import { UserService } from '../../services/user.service';
import { ServiceCardComponent } from '../../components/service-card/service-card.component';
import { UserAuthDto } from '../../models/user-auth-dto';
import { ServiceManagerService } from '../../services/service-manager.service';
import { ServiceDTO } from '../../models/ServiceDto';
@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
  imports:[ServiceCardComponent,RouterLink]
})
export class UserProfileComponent {
  user!:UserAuthDto
  services!:ServiceDTO[]
  
  constructor(private router:Router,private currentUserService:UserService,private servicesManager:ServiceManagerService){
   
  }

  ngOnInit(){
    this.currentUserService.currentUser$.subscribe(u=>{
      if(!u){
        this.router.navigateByUrl("")
      }
      else{
        this.user=u
        if(this.user.userType=="PROVIDER"){
          this.servicesManager.getMyServicesProvider().subscribe(services=>{
          this.services=services
    })
        }
        
      }
    })      
  }


  gotoSchedule(){
    console.log("called")
    this.router.navigateByUrl(`user/${this.user.username}/schedule`)
  }
  
  updateAfterDeleteOfService(id:number){
    this.services=this.services.filter(s=>s.id!=id)
  }

  addService(){
    this.router.navigateByUrl(`/provider/${this.user?.username}/add-service`)
  }
}
