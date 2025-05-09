export class Booking {
    id?: number;
    serviceName?: string;
    providerUserName?: string;
    seekerUserName?: string;
    slotId?: number;
  
    constructor(
      id?: number,
      serviceName?: string,
      providerUserName?: string,
      seekerUserName?: string,
      slotId?: number
    ) {
      this.id = id;
      this.serviceName = serviceName;
      this.providerUserName = providerUserName;
      this.seekerUserName = seekerUserName;
      this.slotId = slotId;
    }
  }
  