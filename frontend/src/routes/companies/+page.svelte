<script>
  import { enhance } from '$app/forms';
  import { invalidateAll } from '$app/navigation';

  export let data; 

  let message = '';

  const onEnhance = (opts) =>
    enhance(opts.form, async ({ result }) => {
      if (result.type === 'success') {
        const r = await result.json();
        if (r.success) {
          message = 'Firma wurde erstellt ✅';
          await invalidateAll();
          opts.form.reset();
        } else {
          message = r.error || 'Erstellen fehlgeschlagen.';
        }
      } else if (result.type === 'failure') {
        message = 'Erstellen fehlgeschlagen.';
      }
    });
</script>

<svelte:head>
  <title>Companies • Freelancer4U</title>
</svelte:head>

<div class="container" style="max-width: 900px; margin: 2rem auto; padding: 1rem;">
  <h1 style="margin-bottom: 1rem;">Companies</h1>

  {#if data.loadError}
    <div style="margin-bottom: 1rem; padding: .75rem; border: 1px solid #f5a; background: #ffe6ee;">
      {data.loadError}
    </div>
  {/if}

  <div style="overflow-x: auto; margin-bottom: 2rem;">
    <table border="1" cellpadding="8" cellspacing="0" width="100%">
      <thead>
        <tr>
          <th align="left">ID</th>
          <th align="left">Name</th>
          <th align="left">Email</th>
        </tr>
      </thead>
      <tbody>
        {#if data.companies && data.companies.length > 0}
          {#each data.companies as c}
            <tr>
              <td>{c.id}</td>
              <td>{c.name}</td>
              <td>{c.email}</td>
            </tr>
          {/each}
        {:else}
          <tr>
            <td colspan="3" align="center">Keine Companies gefunden.</td>
          </tr>
        {/if}
      </tbody>
    </table>
  </div>

  <h2 style="margin-bottom: .5rem;">Create Company</h2>
  <form method="POST" action="?/create" use:onEnhance>
    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px; margin-bottom: 12px;">
      <div>
        <label for="name">Name</label><br />
        <input id="name" name="name" type="text" required style="width: 100%; padding: .5rem;" />
      </div>
      <div>
        <label for="email">Email</label><br />
        <input id="email" name="email" type="email" required style="width: 100%; padding: .5rem;" />
      </div>
    </div>
    <button type="submit" style="padding: .5rem 1rem; cursor: pointer;">Create</button>
  </form>

  {#if message}
    <p style="margin-top: 1rem;">{message}</p>
  {/if}
</div>
