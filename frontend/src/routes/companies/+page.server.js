import axios from "axios";
import { error } from "@sveltejs/kit";
// Load environment variables from .env file for local development
import 'dotenv/config';
const API_BASE_URL = process.env.API_BASE_URL; // defined in frontend/.env

export async function load({ url, locals }) {

    const jwt_token = locals.jwt_token;

    if (!jwt_token) {
        return {
            companies: []
        };
    }

    try {
        // Get URL parameters for pagination
        const currentPage = parseInt(url.searchParams.get('pageNumber') || '1');
        const pageSize = parseInt(url.searchParams.get('pageSize') || '5');

        // Build query string
        let query = `?pageSize=${pageSize}&pageNumber=${currentPage}`;
        const response = await axios({
            method: "get",
            url: `${API_BASE_URL}/api/company` + query,
            headers: { Authorization: "Bearer " + jwt_token },
        });

        return {
            companies: response.data.content,
            nrOfPages: response.data.totalPages || 0,
            currentPage: currentPage,
            pageSize: pageSize
        };

    } catch (axiosError) {
        console.log('Error loading companies:', axiosError);
    }
}

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

        // Validate email first
        const validationResponse = await axios({
            method: "get",
            url: `https://disify.com/api/email/${company.email}`
        });

        console.log("Validated email " + company.email);
        console.log(validationResponse.data);

        if (!validationResponse.data.format || validationResponse.data.disposable || !validationResponse.data.dns) {
            return { success: false, error: `Email ${company.email} is not valid.` };
        }

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
