package ec.net.redcode.tas.on.security;

import ec.net.redcode.tas.on.common.Constant;
import ec.net.redcode.tas.on.persistence.entities.CatalogoItem;
import ec.net.redcode.tas.on.persistence.service.CatalogoItemService;
import org.apache.cxf.rs.security.oauth2.common.*;
import org.apache.cxf.rs.security.oauth2.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;
import org.apache.cxf.rs.security.oauth2.utils.OAuthUtils;
import org.apache.cxf.rs.security.oauth2.utils.crypto.ModelEncryptionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

public class EncryptingDataProvider implements OAuthDataProvider{

    private static final Logger logger = LoggerFactory.getLogger(TasOnSecurityFilter.class);

    private SecretKey key;
    private Map<String, String> clients;
    private Set<String> tokens = new HashSet<>();
    private Map<String, String> refreshTokens = new HashMap<>();
    private String encriptionAlgorithm;
    private CatalogoItemService catalogoItemService;
    private int time;
    private String typeTime;
    private Long lifetime;

    public void initSecretKey(){
        clients = new HashMap<>();
        byte[] decodedKey = Base64.getDecoder().decode(Constant.SECRET_KEY);
        key = new SecretKeySpec(decodedKey, 0, decodedKey.length, this.encriptionAlgorithm);
        List<CatalogoItem> catalogoItem = catalogoItemService.getCatalogoItemByCatalogo(Constant.CATALOGO_SECURITY);
        typeTime = catalogoItem.stream().filter(c -> Constant.SECURITY_TIPO_TIEMPO.equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor();
        time = Integer.valueOf(catalogoItem.stream().filter(c -> Constant.SECURITY_TIEMPO.equals(c.getCatalogoItemDescripcion())).findAny().orElse(null).getCatalogoItemValor());
        TasOnTime tasOnTime = TasOnTime.valueOf(typeTime.toUpperCase());
        Long timeLife;
        switch (tasOnTime){
            case S:
                timeLife =  Long.valueOf(time);
                break;
            case M:
                timeLife =  (time * 60L);
                break;
            case H:
                timeLife = time * 3600L;
                break;
            case D:
                timeLife = time * 86400L;
                break;
            case W:
                timeLife = time * 604800L;
                break;
            default:
                timeLife = 3600L;
                logger.info("Token lifetime default");
                break;
        }
        logger.info("Token lifetime is {} {}", timeLife, typeTime);
        this.lifetime = timeLife;
    }

    @Override
    public Client getClient(String clientId) throws OAuthServiceException {
        return ModelEncryptionSupport.decryptClient(clients.get(clientId), key);
    }

    @Override
    public ServerAccessToken createAccessToken(AccessTokenRegistration accessTokenReg) throws OAuthServiceException {
        String encryptedClient = ModelEncryptionSupport.encryptClient(new Client(accessTokenReg.getClient().getClientId(), accessTokenReg.getClient().getClientSecret(), accessTokenReg.getClient().isConfidential()), key);
        clients.put(accessTokenReg.getClient().getClientId(), encryptedClient);
        ServerAccessToken token = createAccessTokenInternal(accessTokenReg);
        encryptAccessToken(token);
        return token;
    }

    @Override
    public ServerAccessToken getAccessToken(String accessTokenKey) throws OAuthServiceException {
        return ModelEncryptionSupport.decryptAccessToken(this, accessTokenKey, key);
    }

    @Override
    public ServerAccessToken refreshAccessToken(Client client, String refreshToken,
                                                List<String> requestedScopes)
            throws OAuthServiceException {
        String encrypted = refreshTokens.remove(refreshToken);
        ServerAccessToken token = ModelEncryptionSupport.decryptAccessToken(this, encrypted, key);
        tokens.remove(token.getTokenKey());

        // create a new refresh token
        createRefreshToken(token);
        // possibly update other token properties
        encryptAccessToken(token);

        return token;
    }

    @Override
    public void revokeToken(Client client, String token, String tokenTypeHint)
            throws OAuthServiceException {
        // the fast way: if it is the refresh token then there will be a matching value for it
        String accessToken = refreshTokens.remove(token);
        // if no matching value then the token parameter is access token key
        tokens.remove(accessToken == null ? token : accessToken);
    }

    @Override
    public List<OAuthPermission> convertScopeToPermissions(Client client, List<String> requestedScope) {
        // assuming that no specific scopes is documented/supported
        return Collections.emptyList();
    }

    @Override
    public ServerAccessToken getPreauthorizedToken(Client client, List<String> requestedScopes,
                                                   UserSubject subject, String grantType)
            throws OAuthServiceException {
        // This is an optimization useful in cases where a client requests an authorization code:
        // if a user has already provided a given client with a pre-authorized token then challenging
        // a user with yet another form asking for the authorization is redundant
        return null;
    }

    BearerAccessToken createAccessTokenInternal(AccessTokenRegistration accessTokenReg) {
        BearerAccessToken token = new BearerAccessToken(accessTokenReg.getClient(), this.lifetime);
        token.setSubject(accessTokenReg.getSubject());

        createRefreshToken(token);

        token.setGrantType(accessTokenReg.getGrantType());
        token.setAudiences(accessTokenReg.getAudiences());
        token.setParameters(Collections.singletonMap("param", "value"));
        token.setScopes(Collections.singletonList(
                new OAuthPermission("read", "read permission")));
        return token;
    }

    private void encryptAccessToken(ServerAccessToken token) {
        String encryptedToken = ModelEncryptionSupport.encryptAccessToken(token, key);
        tokens.add(encryptedToken);
        refreshTokens.put(token.getRefreshToken(), encryptedToken);
        token.setTokenKey(encryptedToken);
    }

    private void createRefreshToken(ServerAccessToken token) {
        RefreshToken refreshToken = new RefreshToken(token.getClient(),
                "refresh",
                1200L,
                OAuthUtils.getIssuedAt());

        String encryptedRefreshToken = ModelEncryptionSupport.encryptRefreshToken(refreshToken, key);
        token.setRefreshToken(encryptedRefreshToken);
    }

    @Override
    public List<ServerAccessToken> getAccessTokens(Client client, UserSubject sub) throws OAuthServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<RefreshToken> getRefreshTokens(Client client, UserSubject sub) throws OAuthServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    public void setEncriptionAlgorithm(String encriptionAlgorithm) {
        this.encriptionAlgorithm = encriptionAlgorithm;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }

    public void setCatalogoItemService(CatalogoItemService catalogoItemService) {
        this.catalogoItemService = catalogoItemService;
    }
}
