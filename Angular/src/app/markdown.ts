import { MarkedRenderer, MarkedOptions } from 'ngx-markdown';

export function markedOptionsFactory(): MarkedOptions {

    const renderer = new MarkedRenderer();
    renderer.link=(href, title, text) =>
   {
      return "<a href='" + href + "'" + (title ? " title='" + title + "'" : '') + " rel='noopener noreferrer' target='_blank'>" + text + "</a>";
    }
  renderer.table=(header,body) =>
  {
    if (body) body = '<tbody>' + body + '</tbody>';
  
    return '<table class="table table-bordered">\n'
      + '<thead>\n'
      + header
      + '</thead>\n'
      + body
      + '</table>\n';
  }	
  
    
  
    return {
      renderer: renderer,
      gfm: true,
      
    
   
    };
  };