import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../models/User';
import { LoginService } from '../../services/login.service';
import { Provider } from '../../models/Provider';
import { ServiceCardComponent } from '../../components/service-card/service-card.component';
@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
  imports:[ServiceCardComponent]
})
export class UserProfileComponent {
  user:User=new User();
  allUsers:User[];
  providerUser:Provider|null=null
  constructor(private route:ActivatedRoute,private router:Router,private loginService:LoginService){
    const username = this.route.snapshot.paramMap.get('username');
    const raw = localStorage.getItem("users");
    this.allUsers = raw ? JSON.parse(raw):[];
    this.user=this.allUsers.find(u=>u.username==username)!!
  }

  ngOnInit(){
    if(this.user.role=="provider"){
      this.providerUser=this.user as unknown as Provider
    }    
  }

  editProfile() {
    console.log('Edit profile clicked');
  }
  deleteProfile(){
    this.allUsers=this.allUsers.filter(u=>u.username!=this.user.username)
    localStorage.setItem("users",JSON.stringify(this.allUsers))
    this.loginService.handleLogout()
    this.router.navigateByUrl("")
  }
  addService(){
    this.router.navigateByUrl(`/provider/${this.user.username}/add-service`)
  }
}
