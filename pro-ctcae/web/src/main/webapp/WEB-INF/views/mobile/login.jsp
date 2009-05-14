<% response.setContentType("text/vnd.wap.wml"); %>
<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<wml>
    <card id="MainCard" title="ProCtcAE Login">
        <fieldset title="Login">
            Username: <input name="login" value="" format="*m"/>
            Password: <input type="password" name="passwd" value="" format="*m"/>
        </fieldset>
        <anchor title="OK">Log in
            <go method="post" href="/proctcae/pages/j_spring_security_check">
                <postfield name="j_username" value="$(login)"/>
                <postfield name="j_password" value="$(passwd)"/>
            </go>
        </anchor>
    </card>
</wml>