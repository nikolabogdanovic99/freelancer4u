import axios from "axios";
import { error } from "@sveltejs/kit";
import "dotenv/config";

const API_BASE_URL = process.env.API_BASE_URL;

// JWT beim Laden mitschicken
export async function load({ locals }) {
  const jwt_token = locals.jwt_token;
  if (!jwt_token) {
    return { jobs: [], companies: [] };
  }

  try {
    const [jobsResponse, companiesResponse] = await Promise.all([
      axios({
        method: "get",
        url: `${API_BASE_URL}/api/job`,
        headers: { Authorization: "Bearer " + jwt_token }
      }),
      axios({
        method: "get",
        url: `${API_BASE_URL}/api/company`,
        headers: { Authorization: "Bearer " + jwt_token }
      })
    ]);

    return {
      jobs: jobsResponse.data,
      companies: companiesResponse.data
    };
  } catch (axiosError) {
    console.log("Error loading jobs/companies:", axiosError);
    return { jobs: [], companies: [] };
  }
}

export const actions = {
  // JWT beim Erstellen mitschicken
  createJob: async ({ request, locals }) => {
    const jwt_token = locals.jwt_token;
    if (!jwt_token) throw error(401, "Authentication required");

    const data = await request.formData();
    const job = {
      title: data.get("title"),
      description: data.get("description"),
      earnings: parseFloat(data.get("earnings")),
      jobType: data.get("jobType"),
      companyId: data.get("companyId")
    };

    try {
      await axios({
        method: "post",
        url: `${API_BASE_URL}/api/job`,
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + jwt_token
        },
        data: job
      });

      return { success: true };
    } catch (e) {
      console.log("Error creating job:", e);
      return { success: false, error: "Could not create job" };
    }
  }
};
