<script>
  import { enhance } from "$app/forms";
  import axios from "axios";

  let { data, form } = $props();
  let companies = $state(data.companies);
  let currentPage = $state(data.currentPage ?? 1);
  let nrOfPages = $state(data.nrOfPages ?? 0);
  const pageSize = 5; // Anzahl Companies pro Seite

  // Update companies list when data changes (nach Submit oder Navigation)
  $effect(() => {
    companies = data.companies;
    currentPage = data.currentPage ?? 1;
    nrOfPages = data.nrOfPages ?? 0;
  });
</script>


<h1 class="mt-3">Create Company</h1>

{#if form?.success}
  <div class="alert alert-success alert-dismissible fade show" role="alert">
    Company created successfully!
  </div>
{/if}

{#if form?.error}
  <div class="alert alert-danger alert-dismissible fade show" role="alert">
    {form.error}
  </div>
{/if}

<form class="mb-5" method="POST" action="?/createCompany" use:enhance>
  <div class="row mb-3">
    <div class="col">
      <label class="form-label" for="name">Name</label>
      <input class="form-control" id="name" name="name" type="text" required />
    </div>
  </div>
  <div class="row mb-3">
    <div class="col">
      <label class="form-label" for="email">E-Mail</label>
      <input
        class="form-control"
        id="email"
        name="email"
        type="email"
        required
      />
    </div>
  </div>
  <button type="submit" class="btn btn-primary"> Submit </button>
</form>

<h1>All Companies</h1>
<table class="table">
  <thead>
    <tr>
      <th scope="col">Name</th>
      <th scope="col">E-Mail</th>
      <th scope="col">ID</th>
    </tr>
  </thead>
  <tbody>
    {#each companies as company}
      <tr>
        <td>{company.name}</td>
        <td>{company.email}</td>
        <td>{company.id}</td>
      </tr>
    {/each}
  </tbody>
</table>
<h1>All Companies</h1>
<table class="table">
  <thead>
    <tr>
      <th scope="col">Name</th>
      <th scope="col">E-Mail</th>
      <th scope="col">ID</th>
    </tr>
  </thead>
  <tbody>
    {#each companies as company}
      <tr>
        <td>{company.name}</td>
        <td>{company.email}</td>
        <td>{company.id}</td>
      </tr>
    {/each}
  </tbody>
</table>

<nav aria-label="Company list pagination">
  <ul class="pagination">
    {#each Array(nrOfPages) as _, i}
      <li class="page-item">
        <a
          class="page-link {currentPage === i + 1 ? 'active' : ''}"
          href={`/companies?pageNumber=${i + 1}&pageSize=${pageSize}`}
        >
          {i + 1}
        </a>
      </li>
    {/each}
  </ul>
</nav>

<style>
  .page-link:focus {
    box-shadow: none;
  }
</style>
