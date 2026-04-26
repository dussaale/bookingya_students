import { APIRequestContext } from '@playwright/test';

export class ReservationsClient {
    private request: APIRequestContext;
    private readonly endpoint = 'http://localhost:8080/api/reservation';

    constructor(request: APIRequestContext) {
        this.request = request;
    }

    async createReservations(data: any) {
    return await this.request.post(this.endpoint, { data });
    }

    async UpdateReservations(data: any,id: string ) {
    return await this.request.put(`${this.endpoint}/${id}`,{data});
    }

    async getReservations() {
        return await this.request.get(`${this.endpoint}`);
    }

    async getReservationsByGuest(guest: string) {
        return await this.request.get(`${this.endpoint}/guest/${guest}`);
    }

    async getReservationsById(id: string) {
        return await this.request.get(`${this.endpoint}/${id}`);
    }

    async deleteReservationsById(id: string) {
        return await this.request.delete(`${this.endpoint}/${id}`);
    }
}