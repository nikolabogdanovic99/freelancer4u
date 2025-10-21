import 'dotenv/config';

const API_BASE_URL = process.env.API_BASE_URL;

export async function load({ fetch }) {
  const res = await fetch(`${API_BASE_URL}/api/company`);
  if (!res.ok) {
    return { companies: [], loadError: `Konnte Firmen nicht laden (${res.status})` };
  }
  const companies = await res.json();
  return { companies };
}

export const actions = {
  create: async ({ request, fetch }) => {
    const form = await request.formData();
    const name = form.get('name')?.trim();
    const email = form.get('email')?.trim();

    if (!name || !email) {
      return { success: false, error: 'Bitte Name und Email ausfüllen.' };
    }

    const resp = await fetch(`${API_BASE_URL}/api/company`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email })
    });

    if (!resp.ok) {
      const text = await resp.text();
      return { success: false, error: `Anlegen fehlgeschlagen (${resp.status}): ${text}` };
    }

    const created = await resp.json();
    return { success: true, created };
  }
};
