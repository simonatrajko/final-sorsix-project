import { ProviderDTO } from "./ProviderDTO";
export interface ServiceDTO {
    id: number;
    title: string;
    description: string;
    price: number;
    duration: number;
    category: string;
    provider: ProviderDTO;
}
 