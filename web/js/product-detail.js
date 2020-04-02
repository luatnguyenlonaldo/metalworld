window.addEventListener('load', () => {
    loadModel();
}, false);

function loadModel() {
    showLoadingAnimation();

    let urlString = window.location.href; // www.test.com?id=test
    let url = new URL(urlString);
    let modelId = url.searchParams.get('id');

    if (!modelId) {
        return;
    }

    let xhr = new XMLHttpRequest();
    urlString = 'product?id=' + modelId;
    xhr.open('GET', urlString, true);
    xhr.overrideMimeType('application/xml');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let xml = xhr.responseXML;
            if (xml === null) {
                handleEmptyModel();
            } else {
                handleModelDetailReceived(xml);
            }
        }
    };

    xhr.send(null);
}

function handleModelDetailReceived(modelDetail) {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', 'xsl/product-detail.xsl', true);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            applyXslToModelDetail(modelDetail, xhr.responseXML);
        }
    };

    xhr.send(null);
}

function applyXslToModelDetail(modelDetail, xsl) {
    hideLoadingAnimation();

    document.title = modelDetail.getElementsByTagName('main-model')[0]
            .getElementsByTagName('name')[0]
            .innerHTML;

    let xsltProcessor = new XSLTProcessor();
    xsltProcessor.importStylesheet(xsl);

    let pageSize = 18;
    xsltProcessor.setParameter(null, 'pageSize', pageSize);
    xsltProcessor.setParameter(null, 'isRelatedModels', 'true');

    let resultHtml = xsltProcessor.transformToFragment(modelDetail, document);

    let countRelatedModels = modelDetail.getElementsByTagName('model-list')[0].childElementCount;

    let div = document.getElementById('section-model-detail');
    div.innerHTML = '';
    div.appendChild(resultHtml);

    _pageCount = Math.ceil(1.0 * countRelatedModels / pageSize);
    initResultPaging(_pageCount);
}

function handleEmptyModel() {
    hideLoadingAnimation();
    showEmptyModelState();
}

function showEmptyModelState() {
    let div = document.getElementById('model-not-found');
    div.style.display = 'block';
}

function contributionInfor() {
    let email = document.getElementById('email').value;
    let completionTime = document.getElementById('completionTime').value;
    let selectSkillLevel = document.getElementById('skillLevelContribution').value;
    console.log(selectSkillLevel);

    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id = urlParams.get('id');

    let url = 'contributeProduct?email=' + email + "&completionTime=" + completionTime + "&selectSkillLevel=" + selectSkillLevel + "&idProduct=" + id;

    let xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.overrideMimeType('application/xml');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var hiddenPopup = document.getElementById("popup1");
            
            var x = document.getElementById("snackbar");
            x.className = "show";
            setTimeout(function () {
                x.className = x.className.replace("show", "");
            }, 3000);
        }
    };
    xhr.send(null);
}