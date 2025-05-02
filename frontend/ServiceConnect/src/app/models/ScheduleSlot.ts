export  class ScheduleSlot{
    providerUsername?:string;
    startTime?:Date;
    endTime?:Date;
    status?:Status;
    createdAt?:Date;
}

export enum Status{
    free,notFree,requested
}
