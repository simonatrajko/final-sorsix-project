import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/User';
import { BehaviorSubject } from 'rxjs';
import { Service } from '../models/Service';
import { Provider } from '../models/Provider';
import { Booking } from '../models/Booking';
@Injectable({
  providedIn: 'root'
})
export class UserService {

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

  private findAndReplaceWithOld(newUser:User){
    const raw=localStorage.getItem("users")
    const allUsers:User[] = raw ? JSON.parse(raw):[];
    let i=allUsers.findIndex(u=>u.username==newUser.username)
    allUsers[i]=newUser
    localStorage.setItem("users",JSON.stringify(allUsers))

  }
  

  handleAddingNewService(service:Service){
    
    const deconstructed = this._currentUser.value as Provider
    deconstructed.services.push(service)
    this._currentUser.next(deconstructed)
    this.findAndReplaceWithOld(deconstructed)
  }

  handleSignUp(user: User): void {
    const previousUsers = localStorage.getItem('users');
    const users = previousUsers ? JSON.parse(previousUsers) : [];
    if(user.role=="provider"){
      let provider=user as Provider
      provider.services=[]
      users.push(provider)
    }
    else{
      users.push(user);
    }
    localStorage.setItem('users', JSON.stringify(users));
  }
  
  addBooking(booking:Booking){
    const deconstructed = this._currentUser.value as User
    deconstructed.bookings.push(booking)
    this._currentUser.next(deconstructed)
    this.findAndReplaceWithOld(deconstructed)
  }

}



interface LoginInfo{
  username:string,
  password:string
}