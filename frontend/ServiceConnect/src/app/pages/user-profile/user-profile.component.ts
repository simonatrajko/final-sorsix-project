import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../models/User';
import { LoginService } from '../../services/login.service';
@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent {
  user:User=new User();
  allUsers:User[];

  constructor(private route:ActivatedRoute,private router:Router,private loginService:LoginService){
    const username = this.route.snapshot.paramMap.get('username');
    const raw = localStorage.getItem("users");
    this.allUsers = raw ? JSON.parse(raw):[];
    this.user=this.allUsers.find(u=>u.username==username)!!
  }

  editProfile() {
    console.log('Edit profile clicked');
    // Handle the edit profile logic here
  }
  deleteProfile(){
    this.allUsers=this.allUsers.filter(u=>u.username!=this.user.username)
    localStorage.setItem("users",JSON.stringify(this.allUsers))
    this.loginService.handleLogout()
    this.router.navigateByUrl("")
  }
  addService(){}
}
