const loadForm = (link_pessoa) => {
    getForm(link_pessoa);
}

const getForm = (link_pessoa) => {
    $('.root').innerHTML =  `<div class="row fomCadastro">
                                <form class="col s12">
                                    <input id="href" type="hidden">
                                    <div class="row errors hide"></div>
                                    <div class="row success hide"></div>
                                    <div class="row">
                                        <div class="input-field col m6 s12" >
                                            <input id="nome" type="text" class="validate">
                                            <label for="nome">Nome</label>
                                        </div>
                                        <div class="input-field col m6 s12">
                                            <input id="email" type="text" class="validate">
                                            <label for="email">Email</label>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="input-field col m6 s12">
                                            <input id="naturalidade" type="text" class="validate">
                                            <label for="naturalidade">Naturalidade</label>
                                        </div>
                                        <div class="input-field col m6 s12">
                                            <input id="nacionalidade" type="text" class="validate">
                                            <label for="nacionalidade">Nacionalidade</label>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="input-field col m6 s12">
                                            <input id="cpf" type="text" class="validate">
                                            <label for="cpf">CPF</label>
                                        </div>
                                        <div class="input-field col m3 s12">
                                            <input id="dataNascimento" type="text" class="validate">
                                            <label for="dataNascimento">Data Nascimento</label>
                                        </div>
                                        <div class="input-field col m3 s12">
                                            <p>
                                                <label>
                                                    <input name="sexo" type="radio" id="sexoM" class="filled-in"/>
                                                    <span>Homem</span>
                                                </label>
                                                <label style="margin-left: 2.5em;">
                                                    <input name="sexo" type="radio" id="sexoF" class="filled-in"/>
                                                    <span>Mulher</span>
                                                </label>
                                            </p>
                                        </div>
                                        <div class="input-field col m3 s12">
                                            <a class="waves-effect waves-light btn" onclick="salvar()"><i class="material-icons left">save</i>Salvar</a>
                                            <a class="waves-effect waves-light btn red" disabled onclick="excluir()" id="excluir"><i class="material-icons left">delete</i>Excluir</a>
                                        </div>
                                    </div>
                                </form>
                            </div>`;

    if (link_pessoa) {
        const headers = new Headers();
        headers.append('Accept', 'application/json;charset=UTF-8');
        const requestInfo = {
            method: 'GET',
            headers
        };

        fetch(link_pessoa, requestInfo).then((response) => {
            response.ok ? 
                response.json().then(responseBody => {
                    loadPessoa(responseBody)
                }) : response.json().then((responseBody) => showErrors(responseBody.errors));
        })
        .catch((error) => {
            showErrors([error.message]);
        });
    }
    
}

const loadPessoa = (pessoa) => {
    $("#excluir").removeAttribute("disabled");
    $("#href").value = pessoa._links && pessoa._links.self ? pessoa._links.self.href : '';
    $("#nome").value = pessoa.nome !== null && pessoa.nome !== undefined ? pessoa.nome : '';
    $("#email").value = pessoa.email !== null && pessoa.email !== undefined ? pessoa.email : '';
    $("#naturalidade").value = pessoa.naturalidade !== null && pessoa.naturalidade !== undefined ? pessoa.naturalidade : '';
    $("#nacionalidade").value = pessoa.nacionalidade !== null & pessoa.nacionalidade !== undefined ? pessoa.nacionalidade : ''; 
    $("#cpf").value = pessoa.cpf !== null && pessoa.cpf !== undefined ? pessoa.cpf : '';
    $("#dataNascimento").value = pessoa.dataNascimento !== null && pessoa.dataNascimento !== undefined ? pessoa.dataNascimento : '';

    if (pessoa.sexo === 'M') {
        $("#sexoM").checked = true;
    } else if (pessoa.sexo === 'F') {
        $("#sexoF").checked = true;
    }
    document.querySelectorAll("label").forEach(el => {
        if (el.htmlFor && $(`#${el.htmlFor}`).value) {
            el.classList.add('active')
        }
    });
}

const carregarPessoa = () => {
    const pessoa = {_links: {self: { href: null}}};

    pessoa._links.self.href = $("#href").value ? $("#href").value : null;
    pessoa.nome = $("#nome").value;
    pessoa.email = $("#email").value;
    pessoa.naturalidade = $("#naturalidade").value;
    pessoa.nacionalidade = $("#nacionalidade").value;
    pessoa.cpf = $("#cpf").value;
    pessoa.dataNascimento = $("#dataNascimento").value;

    if ( $("#sexoM").checked) {
        pessoa.sexo = 'M';
    } else if ( $("#sexoF").checked){
        pessoa.sexo = 'F';
    }

    return pessoa;
}

const excluded = () => {
    $("#excluir").classList.add('disabled');
    showSuccess(undefined, "Pessoa excluída com sucesso.")
}
const excluir = () => {
    const href =  $("#href").value;
    if (href) {
        const requestInfo = {
            method: 'DELETE',
        };

        fetch(href, requestInfo).then((response) => {
            response.ok ? 
                excluded()
                : response.json().then((responseBody) => showErrors(responseBody.errors));
        })
        .catch((error) => {
            showErrors([error.message]);
        });

    }
}
    
const salvar = () => {
    const pessoa = carregarPessoa();
    const url = pessoa._links.self.href ? pessoa._links.self.href : '/pessoas';

    const headers = new Headers();
    headers.append('Accept', 'application/json;charset=UTF-8');
    headers.append('Content-type', 'application/json');
    const requestInfo = {
        method: pessoa._links.self.href ? 'PUT' : 'POST',
        headers,
        body: JSON.stringify(pessoa)
    };

    fetch(url, requestInfo).then((response) => {
        response.ok ? 
            response.json().then(responseBody => showSuccess(responseBody, "Os dados da pessoa foram salvos com sucesso.")) 
            : response.json().then((responseBody) => showErrors(responseBody.errors));
    })
    .catch((error) => {
         showErrors([error.message]);
    });
}

const showErrors = (errors) => {
    let elementErrors = '';
    errors.forEach(error => {
        elementErrors = `${elementErrors}${error}<br/>`
    })
    $(".errors").innerHTML = elementErrors;
    $(".errors").classList.remove("hide");
    $(".success").classList.add("hide");
}

const showSuccess = (pessoa, mensagem) => {
    $(".success").innerHTML = mensagem;
    $(".success").classList.remove("hide");
    $(".errors").classList.add("hide");
    $("#excluir").removeAttribute("disabled");
    
    if (pessoa && pessoa._links && pessoa._links.self && pessoa._links.self.href) {
        $("#href").value = pessoa._links.self.href;
    } else {
        $("#href").value = undefined;
        loadPessoa({});
    }
}