/** @type {import('./$types').Actions} */
export const actions = {
  default: async ({ request, fetch }) => {
    const formData = await request.formData();
    const message = (formData.get('message') || '').toString().trim();

    if (!message) {
      return {
        success: false,
        error: 'Bitte gib eine Nachricht ein.'
      };
    }

    try {
      const res = await fetch('http://localhost:8080/api/chat', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ message })
      });

      const text = await res.text();

      if (!res.ok) {
        return {
          success: false,
          error: `Fehler vom Backend (Status ${res.status}): ${text || 'kein Text'}`
        };
      }

      let data;
      try {
        data = JSON.parse(text); // { reply: "..." }
      } catch {
        return {
          success: false,
          error: `Antwort vom Backend war kein gültiges JSON: ${text}`
        };
      }

      return {
        success: true,
        question: message,
        answer: data.reply
      };
    } catch (e) {
      return {
        success: false,
        error: 'Fehler beim Aufruf von /api/chat: ' + String(e)
      };
    }
  }
};

export async function load() {
  return {};
}
