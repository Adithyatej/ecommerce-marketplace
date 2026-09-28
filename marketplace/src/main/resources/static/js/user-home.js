/**
 * user-home.js
 * Page-specific behavior for user-home.html.
 *
 * Nothing fetches automatically when the page loads — the grid stays empty
 * until the person clicks a category pill.
 *
 * All five pills share the same fetch/render/pagination logic — only the
 * URL's base differs:
 *   "All Items"          -> GET /api/productsList/view/products
 *   any other category   -> GET /api/productsList/view/products/{category}
 * {category} is the pill's exact displayed text (e.g. "Home and Living"),
 * URL-encoded — encodeURIComponent turns its spaces into %20, per spec.
 *
 * Pagination (same for every pill) is a single "Next" button, not numbered
 * pages:
 *   - paginationTracking is a plain counter starting at 1.
 *   - Clicking "Next" does paginationTracking += 1, then re-fetches.
 *   - page 1       -> no "page" query param at all
 *   - page N, N>1  -> "?page=N" appended to whichever base URL is active
 * "Page 1" here means whatever the backend's own @RequestParam(defaultValue = "0")
 * returns when no param is sent — we never send page=1 or page=0 ourselves.
 *
 * The search box still only filters whatever page of real products is
 * currently on screen — there's no backend search-by-text endpoint, so this
 * doesn't refetch anything, it just hides/shows cards already loaded.
 *
 * Auth: every request goes through App.authFetch, which attaches
 * Authorization: Bearer <token> and auto-refreshes an expired token — see app.js.
 */
const PRODUCTS_API = 'http://localhost:8080/api/productsList';

document.addEventListener('DOMContentLoaded', () => {
  const searchInput = document.getElementById('product-search-input');
  const pills = document.querySelectorAll('.pill-btn');
  const grid = document.getElementById('products-grid');
  const emptyMessage = document.getElementById('no-products-message');
  const paginationEl = document.getElementById('pagination');
  const cartBtn = document.getElementById('cart-btn');

  // null = "All Items"; otherwise the pill's exact displayed label, e.g.
  // "Sports and Fitness" — that's what gets sent as the {category} path variable.
  let activeCategoryName = null;
  let paginationTracking = 1; // current page number; starts at 1

  // productListingResponse has no image field, so every card gets the same
  // neutral placeholder icon (an inline SVG, so there's no file to go missing).
  const PLACEHOLDER_IMAGE =
    "data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='%2394a3b8' stroke-width='1.5'><rect x='3' y='3' width='18' height='18' rx='2'/><circle cx='8.5' cy='8.5' r='1.5'/><path d='M21 15l-5-5L5 21'/></svg>";

  function normalizeCategory(value) {
    return (value || '').toLowerCase().trim().replace(/[^a-z0-9]+/g, '-').replace(/(^-|-$)/g, '');
  }

  function escapeHtml(value) {
    const div = document.createElement('div');
    div.textContent = value == null ? '' : String(value);
    return div.innerHTML;
  }

  // Builds the URL for whichever pill is active, plus the shared pagination
  // contract — no query string at all for page 1.
  function buildProductsUrl(page) {
    const base = activeCategoryName
      ? `${PRODUCTS_API}/view/products/${encodeURIComponent(activeCategoryName)}`
      : `${PRODUCTS_API}/view/products`;
    return page > 1 ? `${base}?page=${page}` : base;
  }

  function buildCard(product) {
    const card = document.createElement('div');
    card.className = 'product-card';
    card.dataset.category = normalizeCategory(product.mainProductCategory);
    // Not linked to a detail page yet — that comes once a product-detail
    // page exists. For now this just carries the id so that link can be
    // added here later without re-touching the card markup.
    card.dataset.productId = product.id;

    const categoryLabel = [product.mainProductCategory, product.subProductCategory]
      .filter(Boolean)
      .join(' · ');

    card.innerHTML = `
      <div class="product-img">
        <img src="${PLACEHOLDER_IMAGE}" alt="" class="product-img-placeholder" />
        <span class="vendor-badge">🏪 ${escapeHtml(product.brand || 'Unknown seller')}</span>
      </div>
      <div class="product-body">
        <span class="product-category">${escapeHtml(categoryLabel)}</span>
        <h3 class="product-title">${escapeHtml(product.productName || 'Untitled product')}</h3>
        <p style="font-size: 0.85rem; color: #64748b; margin-bottom: 1rem;">
          ${escapeHtml(product.productDescription || '')}
        </p>
        <div class="product-meta">
          <span class="product-price">$${Number(product.price ?? 0).toFixed(2)}</span>
          <button class="btn btn-primary btn-sm btn-add-cart" data-product-id="${escapeHtml(product.id)}">+ Add to Cart</button>
        </div>
      </div>
    `;
    return card;
  }

  function renderProducts(products) {
    grid.querySelectorAll('.product-card').forEach((card) => card.remove());
    products.forEach((product) => grid.insertBefore(buildCard(product), emptyMessage));
    emptyMessage.textContent = 'No products found.';
    emptyMessage.style.display = products.length === 0 ? 'block' : 'none';
  }

  // Single "Next" button — no page numbers. Disabled once a page comes back
  // empty, so clicking it can't page into nothing forever.
  function renderPagination(itemCount) {
    paginationEl.innerHTML = '';

    const nextBtn = document.createElement('button');
    nextBtn.type = 'button';
    nextBtn.className = 'pagination-btn';
    nextBtn.textContent = 'Next ›';
    nextBtn.disabled = itemCount === 0;
    nextBtn.addEventListener('click', () => {
      paginationTracking += 1; // i = i + 1
      loadProducts(paginationTracking);
    });

    paginationEl.appendChild(nextBtn);
  }

  // Loads a page for whichever pill is active (activeCategoryName). Shared by
  // "All Items" and all four categories — only buildProductsUrl differs.
  // Only ever called from a click — never automatically on page load.
  async function loadProducts(page) {
    grid.querySelectorAll('.product-card').forEach((card) => card.remove());
    paginationEl.innerHTML = '';
    emptyMessage.textContent = 'Loading products…';
    emptyMessage.style.display = 'block';

    try {
      // App.authFetch attaches "Authorization: Bearer <token>" for us, and
      // auto-refreshes the token first if the backend says it's expired.
      const response = await App.authFetch(buildProductsUrl(page));

      if (!response.ok) {
        emptyMessage.textContent = `Could not load products (${response.status}).`;
        return;
      }

      const data = await response.json();
      // Spring Data's Page<T> shape has "content"; fall back to treating the
      // body as a bare array in case it's ever returned that way instead.
      const items = Array.isArray(data) ? data : data.content || [];

      renderProducts(items);
      renderPagination(items.length);

      // Re-apply an in-progress search so it still applies after a category
      // switch or a "Next" click, instead of silently resetting.
      if (searchInput.value.trim() !== '') applySearchFilter();
    } catch (err) {
      emptyMessage.textContent = 'Could not reach the server. Is the backend running on localhost:8080?';
    }
  }

  // The search box only filters whatever page of real products is already
  // on screen — there's no backend search-by-text endpoint, so this never
  // refetches anything.
  function applySearchFilter() {
    const query = searchInput.value.trim().toLowerCase();
    const cards = grid.querySelectorAll('.product-card');
    let visibleCount = 0;

    cards.forEach((card) => {
      const title = card.querySelector('.product-title').textContent.toLowerCase();
      const category = card.querySelector('.product-category').textContent.toLowerCase();
      const visible = query === '' || title.includes(query) || category.includes(query);
      card.style.display = visible ? '' : 'none';
      if (visible) visibleCount += 1;
    });

    if (cards.length > 0) {
      emptyMessage.textContent = 'No products match your search.';
      emptyMessage.style.display = visibleCount === 0 ? 'block' : 'none';
    }
  }

  pills.forEach((pill) => {
    pill.addEventListener('click', () => {
      pills.forEach((p) => p.classList.remove('active'));
      pill.classList.add('active');

      // "all" has no path segment; every other pill sends its own visible
      // label ("Electronics", "Home and Living", ...) as the {category} path
      // variable — exactly what's displayed on the button.
      activeCategoryName = pill.dataset.category === 'all' ? null : pill.textContent.trim();
      paginationTracking = 1; // fresh browse — back to page 1
      loadProducts(paginationTracking);
    });
  });

  searchInput.addEventListener('input', applySearchFilter);

  if (cartBtn) {
    cartBtn.addEventListener('click', () => {
      window.location.href = 'user-cart.html';
    });
  }

  // Deliberately no loadProducts() call here — the grid stays empty until
  // the person clicks a category pill.
});