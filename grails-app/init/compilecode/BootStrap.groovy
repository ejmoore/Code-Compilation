package compilecode

class BootStrap {

    def init = { servletContext ->
        def userRole = new Role(authority:'ROLE_USER').save()
        def me = new User(username:'user', passowrd:'test').save()

        UserRole.create me, userRole

        UserRole.withSession {
            it.flush()
            it.clear()
        }
    }
    def destroy = {
    }
}
