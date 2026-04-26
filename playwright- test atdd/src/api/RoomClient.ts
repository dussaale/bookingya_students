import { APIRequestContext } from '@playwright/test';

export class RoomClient {
    private request: APIRequestContext;
    private readonly endpoint = 'http://localhost:8080/api/room';

    constructor(request: APIRequestContext) {
        this.request = request;
    }

    async createRoom(data: any) {
    return await this.request.post(this.endpoint, { data });
    }

    async getRoomByCode(code: string) {
        return await this.request.get(`${this.endpoint}/code/${code}`);
    }

    async deleteBooking(id: string) {
        return await this.request.delete(`${this.endpoint}/${id}`);
    }
}