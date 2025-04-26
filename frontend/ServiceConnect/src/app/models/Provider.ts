import { User } from './User';
import { Service } from './Service';
export class Provider extends User {
  services: Service[] = [];

  constructor(
    id: number = 0,
    username: string = "",
    email: string = "",
    password: string = "",
    location: string = "",
    role: string = "",
    pictureUrl: string = "",
    fullName: string = ""
  ) {
    super(id, username, email, password, location, role, pictureUrl, fullName);
  }

  addService(service: Service): void {
    this.services.push(service);
  }

  removeService(serviceId: number): void {
    this.services = this.services.filter(service => service.id !== serviceId);
  }
}
