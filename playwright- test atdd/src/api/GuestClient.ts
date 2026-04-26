import { APIRequestContext } from '@playwright/test';

export class GuestClient {
    private request: APIRequestContext;
    private readonly endpoint = 'http://localhost:8080/api/guest';

    constructor(request: APIRequestContext) {
        this.request = request;
    }

    async createGuest(data: any) {
    return await this.request.post(this.endpoint, { data });
    }

    async getGuestIdentification(identifiation: string) {
        return await this.request.get(`${this.endpoint}/identifiation/${identifiation}`);
    }

    async deleteBooking(id: string) {
        return await this.request.delete(`${this.endpoint}/${id}`);
    }
}