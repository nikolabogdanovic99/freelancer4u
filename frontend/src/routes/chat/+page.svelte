<script>
  // WICHTIG: Bei Form Actions heisst das Objekt "form", nicht "data"
  export let form;
</script>

<h1>💬 Chatbot</h1>

<form method="POST">
  <div class="mb-3">
    <label for="message" class="form-label">Deine Frage an den Chatbot</label>
    <textarea
      id="message"
      name="message"
      class="form-control"
      rows="3"
      placeholder="Frag mich nach Jobs, Firmen oder lass einen neuen Job erstellen..."
    ></textarea>
  </div>

  <button type="submit" class="btn btn-primary">Senden</button>
</form>

{#if form?.error}
  <div class="alert alert-danger mt-3">
    {form.error}
  </div>
{/if}

{#if form?.success}
  <div class="card mt-4">
    <div class="card-body">
      <h5 class="card-title">Letzte Konversation</h5>
      <p><strong>Du:</strong> {form.question}</p>
      <p><strong>Chatbot:</strong> {form.answer}</p>
    </div>
  </div>
{/if}
