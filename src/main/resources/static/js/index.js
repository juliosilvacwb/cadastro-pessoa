const $ = (seletor) => {
    let ref = undefined;
    ref = document.querySelector(seletor);

    if(ref == undefined) {
        throw `Não foi possível identificar o item ${seletor} no DOM`;
    }

    return ref;
}

function ready(callbackFunc) {
    if (document.readyState !== 'loading') {
        callbackFunc();
    } else if (document.addEventListener) {
        document.addEventListener('DOMContentLoaded', callbackFunc);
    } else {
        document.attachEvent('onreadystatechange', function () {
            if (document.readyState === 'complete') {
                callbackFunc();
            }
        });
    }
}

document.addEventListener('DOMContentLoaded', function () {
    var elems = document.querySelectorAll('.sidenav');
    var instances = M.Sidenav.init(elems, {});
});

ready(function() {
    loadForm();
});
