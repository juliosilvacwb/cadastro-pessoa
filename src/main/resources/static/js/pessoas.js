const loadPessoas = () => {

    const url = '/pessoas';

    const headers = new Headers();
    headers.append('Accept', 'application/json;charset=UTF-8');
    const requestInfo = {
        method: 'GET',
        headers
    };

    fetch(url, requestInfo).then((response) => {
        response.ok ? 
            response.json().then(responseBody => {
                $('.root').innerHTML = getTable(responseBody._embedded.pessoas)
            }) : response.json().then((responseBody) => showErrors(responseBody.errors));
    })
    .catch((error) => {
         showErrors([error.message]);
    });

}

const getTable = (pessoas) => {
    return `<table>
                <thead>
                <tr>
                    <th>Nome</th>
                    <th>Sexo</th>
                    <th>Email</th>
                    <th>Data de Nascimento</th>
                    <th>Naturalidade</th>
                    <th>Nacionalidade</th>
                    <th>CPF</th>
                    <th>Ação</th>
                </tr>
                </thead>
                <tbody>
                    ${ getRows(pessoas) }
                </tbody>
            </table>`
}

const getRows = (pessoas) => {

    if(pessoas === undefined || pessoas.length === 0) {
        return  `<tr>
                    <td colspan="8">Nenhum registro encontrado.</td>
                </tr>`
    }

    let tds = '';
    pessoas.forEach(pessoa => {
        tds = tds + `<tr>
                        <td>${ pessoa.nome !== null && pessoa.nome !== undefined ? pessoa.nome : '' }</td>
                        <td>${ pessoa.sexo !== null && pessoa.sexo !== undefined ? pessoa.sexo : '' }</td>
                        <td>${ pessoa.email !== null && pessoa.email !== undefined ? pessoa.email : ''}</td>
                        <td>${ pessoa.dataNascimento !== null && pessoa.dataNascimento !== undefined ? pessoa.dataNascimento : '' }</td>
                        <td>${ pessoa.naturalidade !== null && pessoa.naturalidade !== undefined ? pessoa.naturalidade : '' }</td>
                        <td>${ pessoa.nacionalidade !== null && pessoa.nacionalidade !== undefined ? pessoa.nacionalidade : ''}</td>
                        <td>${ pessoa.cpf }</td>
                        <td>
                            <a class="btn-floating btn-large waves-effect waves-light btn-small" onclick="loadForm('${pessoa._links.self.href}')">
                                <i class="material-icons">edit</i>
                            </a>
                        </td>
                    </tr>`
    });

    return  tds;
}