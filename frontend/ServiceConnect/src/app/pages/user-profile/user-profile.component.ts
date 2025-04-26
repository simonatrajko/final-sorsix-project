import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from '../../models/User';
@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent {
  user:User=new User();

  constructor(private route:ActivatedRoute){}

  ngOnInit() {
    const username = this.route.snapshot.paramMap.get('username');
    const raw = localStorage.getItem("users");
    const allUsers:User[] = raw ? JSON.parse(raw):[];
    this.user=allUsers.find(u=>u.username==username)!!
  }

  editProfile() {
    console.log('Edit profile clicked');
    // Handle the edit profile logic here
  }
  deleteProfile(){}
  addService(){}
}
