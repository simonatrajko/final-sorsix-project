import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/User';
import { BehaviorSubject } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private router:Router) { }

  private _currentUser=new BehaviorSubject<User|null>(null)
  currentUser$=this._currentUser.asObservable()

  handleLogin(loginInfo:LoginInfo){
    const raw = localStorage.getItem("users");
    const allUsers:User[] = raw ? JSON.parse(raw):[];
    let username:string=""
    let user:User|null=null
    let found=false
    for(let i=0;i<allUsers.length;i++){
      if(allUsers[i].password==loginInfo.password && allUsers[i].username==loginInfo.username){
        found=true
        username=loginInfo.username
        user=allUsers[i]
      }
    }
    if(found){
      this.router.navigate(["user",username])
      this._currentUser.next(user)
    }
    else{
      alert("invalid username or password")
    }
  }
  
  handleLogout(){
    this._currentUser.next(null)
  }
}

interface LoginInfo{
  username:string,
  password:string
}