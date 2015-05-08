/*
 * Include the core server components.
 * Michael Gottburg - Etudiant CPLN
 * Test Evalutaion
 */

#include "httpd.h"
#include "http_config.h"
#include <string.h>

static int mod_myapp_method_handler (request_rec *r)
{
    /* First off, we need to check if this is a call for the "example-handler" handler.
     * If it is, we accept it and do our things, if not, we simply return DECLINED,
     * and the server will try somewhere else.
     */
    if (!r->handler || strcmp(r->handler, "myapp-handler")) return (DECLINED);
    
    /* Now that we are handling this request, we'll write out "Hello, world!" to the client.
     * To do so, we must first set the appropriate content type, followed by our output.
     */
    ap_set_content_type(r, "text/html");
    
    if (r->args != "") {
      char * query = r->args;
      char * pos = strchr (query, '=');
      ap_rprintf(r, "Hello %s how are you ?",++pos);
    }
    else {
      ap_rprintf(r, "You don't have a name ? Really ?!");
    }
    
    /* Lastly, we must tell the server that we took care of this request and everything went fine.
     * We do so by simply returning the value OK to the server.
     */
    return OK;
}

static void mod_myapp_register_hooks (apr_pool_t *pool)
{
  // I think this is the call to make to register a
  // handler for method calls (GET PUT et. al.).
  // We will ask to be last so that the comment
  // has a higher tendency to go at the end.
  ap_hook_handler(mod_myapp_method_handler, NULL, NULL, APR_HOOK_LAST);
}

module AP_MODULE_DECLARE_DATA   mod_myapp =
{
  // Only one callback function is provided.  Real
  // modules will need to declare callback functions for
  // server/directory configuration, configuration merging
  // and other tasks.
  STANDARD20_MODULE_STUFF,
  NULL,
  NULL,
  NULL,
  NULL,
  NULL,
  mod_myapp_register_hooks,      /* callback for registering hooks */
};
