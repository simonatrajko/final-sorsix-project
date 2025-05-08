import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/User';
import { BehaviorSubject } from 'rxjs';
import { Service } from '../models/Service';
import { Provider } from '../models/Provider';
import { Booking } from '../models/Booking';
import { UserAuthDto } from '../models/user-auth-dto';
import { AuthService } from './auth-service.service';
@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private router:Router,private authService:AuthService) { 
    if(localStorage.getItem("currentUser")){
      const asString=localStorage.getItem("currentUser")!
      const asObject=JSON.parse(asString) as UserAuthDto
      this._currentUser.next(asObject)
    }
  }

  private _currentUser=new BehaviorSubject<UserAuthDto|null>(null)
  currentUser$=this._currentUser.asObservable()
  

  handleLogin(loginInfo:any){
    console.log(loginInfo)
    this._currentUser.next(loginInfo)
    localStorage.setItem("currentUser",JSON.stringify(loginInfo))
  }
  
  handleLogout(){
    this._currentUser.next(null)
    this.authService.logout()
    localStorage.removeItem("currentUser")
  }


  setCurrentUser(newUser:UserAuthDto){
    this._currentUser.next(newUser)
  }

  

  handleAddingNewService(service:Service){
    
    // const deconstructed = this._currentUser.value as Provider
    // deconstructed.services.push(service)
    // this._currentUser.next(deconstructed)
    // this.findAndReplaceWithOld(deconstructed)
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
    // const deconstructed = this._currentUser.value as User
    // deconstructed.bookings.push(booking)
    // this._currentUser.next(deconstructed)
    // this.findAndReplaceWithOld(deconstructed)
  }

}



interface LoginInfo{
  username:string,
  password:string
}