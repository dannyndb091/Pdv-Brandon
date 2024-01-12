package com.pdv.Test.Service;

import com.pdv.Test.Models.ClienteProveedor;
import com.pdv.Test.Models.DTOs.Clientes.UpdateClient;
import com.pdv.Test.Repository.ClienteRepository;
import com.pdv.Test.Repository.DocumentosRepository;
import com.pdv.Test.Service.Others.Verifiers;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ClientesService {
    private final ClienteRepository cliRep;
    private final DocumentosRepository docRep;
    private final Verifiers verifiers;

    @Transactional
    public ResponseEntity<Object> newClient(ClienteProveedor cli) {
        return newClientTransaction(cli);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> newClientTransaction(ClienteProveedor cli) {
        try {
            verifyClientData(cli);

            ClienteProveedor cliprov = cliRep.findByClCode(cli.getClCode());
            if (cliprov != null) throw new RuntimeException("«ERR-X05»");
            cliprov = cliRep.findByClMail(cli.getClMail());
            if (cliprov != null) throw new RuntimeException("«ERR-X06»");
            cliprov = cliRep.findByClCellphone(cli.getClCellphone());
            if (cliprov != null) throw new RuntimeException("«ERR-X07»");

            cliprov = cliRep.save(cli);

            return ResponseEntity.ok(ClienteProveedor.builder()
                    .clCode(cliprov.getClCode())
                    .clName(cliprov.getClName())
                    .clMail(cliprov.getClMail())
                    .clCellphone(cliprov.getClCellphone()).build());

        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    @Transactional
    public ResponseEntity<Object> updateClient(UpdateClient uCli) {
        return updateClientTransaction(uCli);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> updateClientTransaction(UpdateClient uCli) {
        try {
            verifyClientData(ClienteProveedor.builder()
                    .clCode(uCli.getNewCode())
                    .clName(uCli.getNewName())
                    .clMail(uCli.getNewMail())
                    .clCellphone(uCli.getNewCellphone())
                    .clTypeClient(uCli.getNewTypeClient()).build());

            ClienteProveedor oldCli = cliRep.findByClCode(uCli.getOldCode());
            if (oldCli == null) throw new RuntimeException("«ERR-X09»");

            ClienteProveedor newCli = cliRep.findByClCode(uCli.getNewCode());
            if (newCli != null && !oldCli.getClId().equals(newCli.getClId())) throw new RuntimeException("«ERR-X05»");
            newCli = cliRep.findByClMail(uCli.getNewMail());
            if (newCli != null  && !oldCli.getClId().equals(newCli.getClId())) throw new RuntimeException("«ERR-X06»");
            newCli = cliRep.findByClCellphone(uCli.getNewCellphone());
            if (newCli != null  && !oldCli.getClId().equals(newCli.getClId())) throw new RuntimeException("«ERR-X07»");

            cliRep.save(ClienteProveedor.builder()
                    .clId(oldCli.getClId())
                    .clCode(uCli.getNewCode())
                    .clName(uCli.getNewName())
                    .clMail(uCli.getNewMail())
                    .clCellphone(uCli.getNewCellphone())
                    .clTypeClient(uCli.getNewTypeClient())
                    .clStatus(uCli.getNewStatus()).build());

            return ResponseEntity.ok(ClienteProveedor.builder()
                    .clCode(uCli.getNewCode())
                    .clName(uCli.getNewName())
                    .clMail(uCli.getNewMail())
                    .clCellphone(uCli.getNewCellphone()).build());

        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    private void verifyClientData(ClienteProveedor cli) {
        if (!verifiers.verifyCellphone(cli.getClCellphone())) throw new RuntimeException("«ERR-X01»");
        if (!verifiers.verifyMail(cli.getClMail())) throw new RuntimeException("«ERR-X02»");
        if (!verifiers.verifyClCode(cli.getClCode())) throw new RuntimeException("«ERR-X03»");
        if (!verifiers.verifyClName(cli.getClName())) throw new RuntimeException("«ERR-X04»");
        if (!(cli.getClTypeClient() != null
                && cli.getClTypeClient() >= 0
                && cli.getClTypeClient() <= 2)) throw new RuntimeException("«ERR-X08»");
    }

    @Transactional
    public ResponseEntity<Object> delClient(ClienteProveedor cli) {
        return delClientTransaction(cli);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> delClientTransaction(ClienteProveedor cli) {
        try{
            ClienteProveedor cliProv = cliRep.findByClCode(cli.getClCode());
            if (cliProv == null) throw new RuntimeException("«ERR-X10»");
            if (!cli.getClName().equals(cliProv.getClName())) throw new RuntimeException("«ERR-X11»");
            if (docRep.countByDocIdCliente(cliProv.getClId()) > 0) throw new RuntimeException("«ERR-X12»");

            cliRep.delete(cliProv);

            return ResponseEntity.ok("");
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }
}
