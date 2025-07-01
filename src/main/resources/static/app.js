const apiBaseUrl = 'http://localhost:8080/api/v1/transaction';

let transactionListDTO = {
    page: 0,
    size: 10,
    currentPage: 1,
    totalPages: 0,
    totalElements: 0
};

// 获取URL参数
function getQueryParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

// 初始化分页参数
function initPagination() {
    const page = getQueryParam('page');
    if (page) {
        transactionListDTO.page = parseInt(page) - 1; // 后端通常从0开始计数
        transactionListDTO.currentPage = parseInt(page);
    }
    
    const size = getQueryParam('size');
    if (size) {
        transactionListDTO.size = parseInt(size);
    }
}

// 更新URL参数
function updateUrlParams() {
    const params = new URLSearchParams();
    params.set('page', transactionListDTO.currentPage);
    params.set('size', transactionListDTO.size);
    window.history.replaceState({}, '', '?' + params.toString());
}

// 跳转到指定页面
function goToPage(page) {
    if (page < 1 || page > transactionListDTO.totalPages) return;
    
    transactionListDTO.currentPage = page;
    transactionListDTO.page = page - 1; // 后端通常从0开始计数
    updateUrlParams();
    fetchTransactions();
}

// 上一页
function prevPage() {
    if (transactionListDTO.currentPage > 1) {
        goToPage(transactionListDTO.currentPage - 1);
    }
}

// 下一页
function nextPage() {
    if (transactionListDTO.currentPage < transactionListDTO.totalPages) {
        goToPage(transactionListDTO.currentPage + 1);
    }
}

// 初始化事件监听器
function initEventListeners() {
    document.getElementById('prevPage').addEventListener('click', prevPage);
    document.getElementById('nextPage').addEventListener('click', nextPage);
    document.getElementById('pageInput').addEventListener('change', function(e) {
        const page = parseInt(e.target.value);
        if (!isNaN(page)) {
            goToPage(page);
        }
    });
    
    // 搜索功能
    document.querySelector('#transactionTable input[name="keyword"]')?.addEventListener('input', function(e) {
        transactionListDTO.keyword = e.target.value;
        transactionListDTO.currentPage = 1;
        transactionListDTO.page = 0;
        updateUrlParams();
        fetchTransactions();
    });
}

// 更新分页信息显示
function updatePaginationDisplay() {
    document.getElementById('pageInput').value = transactionListDTO.currentPage;
    document.getElementById('totalPages').textContent = transactionListDTO.totalPages;
}

// 获取所有交易
async function fetchTransactions() {
    try {
        const response = await fetch(`/api/v1/transaction?page=${transactionListDTO.currentPage}&size=${transactionListDTO.size}${transactionListDTO.keyword ? '&keyword=' + transactionListDTO.keyword : ''}`);
        if (!response.ok) throw new Error('Network response was not ok');
        
        const data = await response.json();
        
        // 更新分页信息
        transactionListDTO.totalElements = data.total;
        transactionListDTO.totalPages = data.totalPages;
        transactionListDTO.currentPage = data.currentPage;
        
        // 更新页面显示
        updatePaginationDisplay();
        renderTransactions(data.records);
    } catch (error) {
        console.error('Error fetching transactions:', error);
    }
}

// 渲染交易列表
function renderTransactions(transactions) {
    const tbody = document.querySelector("#transactionTable tbody");
    tbody.innerHTML = '';
    
    if (!transactions || transactions.length === 0) {
        const tr = document.createElement('tr');
        tr.innerHTML = '<td colspan="12">No transactions found</td>';
        tbody.appendChild(tr);
        return;
    }
    
    transactions.forEach(transaction => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${transaction.transactionId}</td>
            <td>${transaction.remitterAccount}</td>
            <td>${transaction.fromCurrency}</td>
            <td>${transaction.fromAmount}</td>
            <td>${transaction.beneficiaryAccount}</td>
            <td>${transaction.toCurrency}</td>
            <td>${transaction.toAmount}</td>
            <td>${transaction.transactionStatus}</td>
            <td>
                <button onclick="editTransaction('${transaction.transactionId}')">Edit</button>
                <button onclick="deleteTransaction('${transaction.transactionId}')">Delete</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// 提交新增或更新数据
document.getElementById('transactionForm').addEventListener('submit', function (e) {
  e.preventDefault();
  const formData = new FormData(this);
  const data = Object.fromEntries(formData);

  // 确保数值类型正确转换
  data.fromAmount = parseFloat(data.fromAmount);
  data.toAmount = parseFloat(data.toAmount);
  data.exchangeRate = parseFloat(data.exchangeRate);
  data.fee = parseFloat(data.fee);

  const method = this.dataset.id ? 'PUT' : 'POST';
  const url = this.dataset.id ? `${apiBaseUrl}/${this.dataset.id}` : apiBaseUrl;

  fetch(url, {
    method: method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  }).then(() => {
    this.reset();
    delete this.dataset.id;
    fetchTransactions();
  });
});

// 删除交易
function deleteTransaction(id) {
  if (confirm('Are you sure you want to delete this transaction?')) {
    fetch(`${apiBaseUrl}/${id}`, { method: 'DELETE' })
      .then(() => fetchTransactions());
  }
}

// disable the transactionId input
function editTransaction(id) {
  fetch(`${apiBaseUrl}/${id}`)
    .then(res => res.json())
    .then(t => {
      const form = document.getElementById('transactionForm');
      form.transactionId.value = t.transactionId;
      form.remitterAccount.value = t.remitterAccount;
      form.fromCurrency.value = t.fromCurrency;
      form.fromAmount.value = t.fromAmount;
      form.beneficiaryAccount.value = t.beneficiaryAccount;
      form.toCurrency.value = t.toCurrency;
      form.toAmount.value = t.toAmount;
      form.exchangeRate.value = t.exchangeRate;
      form.feeCurrency.value = t.feeCurrency;
      form.fee.value = t.fee;
      form.transactionStatus.value = t.transactionStatus;
      form.transactionType.value = t.transactionType;
      form.comments.value = t.comments;
      form.dataset.id = t.transactionId;
    });
}

// 初始化加载数据
async function init() {
    initPagination();
    initEventListeners();
    await fetchTransactions();
}

// 启动应用
init();
