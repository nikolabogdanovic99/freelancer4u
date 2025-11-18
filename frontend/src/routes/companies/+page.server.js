import axios from "axios";
import { error } from "@sveltejs/kit";
import 'dotenv/config';

const API_BASE_URL = process.env.API_BASE_URL;

export async function load({ url, locals }) {
    const jwt_token = locals.jwt_token;

    if (!jwt_token) {
        return {
            companies: [],
            currentPage: 1,
            nrOfPages: 0
        };
    }

    try {
        // Pagination-Parameter aus der URL lesen
        const currentPage = parseInt(url.searchParams.get('pageNumber') || '1');
        const pageSize = parseInt(url.searchParams.get('pageSize') || '5');

        const query = `?pageNumber=${currentPage}&pageSize=${pageSize}`;

        const response = await axios({
            method: "get",
            url: `${API_BASE_URL}/api/company` + query,
            headers: { Authorization: "Bearer " + jwt_token },
        });

        return {
            companies: response.data.content,    // wie bei jobs
            currentPage,
            nrOfPages: response.data.totalPages || 0
        };

    } catch (axiosError) {
        console.log('Error loading companies:', axiosError);
        return {
            companies: [],
            currentPage: 1,
            nrOfPages: 0
        };
    }
}

export const actions = {
    createCompany: async ({ request, locals }) => {

        const jwt_token = locals.jwt_token;

        if (!jwt_token) {
            throw error(401, 'Authentication required');
        }

        const data = await request.formData();
        const company = {
            name: data.get('name'),
            email: data.get('email')
        };

        try {
            await axios({
                method: "post",
                url: `${API_BASE_URL}/api/company`,
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + jwt_token,
                },
                data: company,
            });
            return { success: true };
        } catch (error) {
            console.log('Error creating company:', error);
            return { success: false, error: 'Could not create company' };
        }
    }
};
