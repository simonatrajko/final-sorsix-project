import { Component } from '@angular/core';
import { ActivatedRoute, Router,RouterLink } from '@angular/router';
import { UserService } from '../../services/user.service';
import { ServiceCardComponent } from '../../components/service-card/service-card.component';
import { UserAuthDto } from '../../models/user-auth-dto';
@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
  imports:[ServiceCardComponent,RouterLink]
})
export class UserProfileComponent {
  user!:UserAuthDto
  
  constructor(private route:ActivatedRoute,private router:Router,private currentUserService:UserService){
   
  }

  ngOnInit(){
    this.currentUserService.currentUser$.subscribe(u=>{
      if(!u){
        this.router.navigateByUrl("")
      }
      else{
        this.user=u
      }
    })  
  }

  deleteProfile(){
    
  }
  addService(){
    this.router.navigateByUrl(`/provider/${this.user?.username}/add-service`)
  }
}
