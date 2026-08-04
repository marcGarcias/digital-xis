import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CMSService } from '../../../services/CMSService';

interface FaqItem {
  pergunta: string;
  resposta: string;
  aberta: boolean;
}

@Component({
  selector: 'app-faq',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './faq.html',
  styleUrls: ['./faq.css'],
})
export class FaqComponent implements OnInit {
  faqs: FaqItem[] = [];
  loading = true;

  private fallbackFaqs: FaqItem[] = [
    { pergunta: 'Quais serviços vocês oferecem?', resposta: 'Trabalhamos com gestão de redes sociais, tráfego pago, criação de conteúdo, identidade visual, design para posts, consultoria estratégica e lançamentos.', aberta: true },
    { pergunta: 'Vocês atendem empresas de qualquer segmento?', resposta: 'Sim! Atendemos negócios locais, marcas pessoais, e-commerces e empresas de diversos nichos.', aberta: false },
    { pergunta: 'Como funciona o processo após o primeiro contato?', resposta: 'Após o envio do formulário, nossa equipe analisa sua demanda e entra em contato para entender melhor seus objetivos e apresentar uma proposta personalizada.', aberta: false },
    { pergunta: 'Trabalham com contratos mensais ou projetos pontuais?', resposta: 'Oferecemos tanto planos mensais quanto serviços pontuais, dependendo da necessidade do cliente.', aberta: false },
    { pergunta: 'Qual é o prazo médio para início do serviço?', resposta: 'Normalmente iniciamos em até 5 a 10 dias úteis após a aprovação da proposta e assinatura do contrato.', aberta: false },
    { pergunta: 'Preciso já ter redes sociais ou site para contratar vocês?', resposta: 'Não! Podemos te ajudar desde o zero ou otimizar canais que você já possui.', aberta: false },
    { pergunta: 'Vocês garantem resultados?', resposta: 'Não garantimos números específicos, mas trabalhamos com estratégias validadas, dados e acompanhamento constante para alcançar os melhores resultados possíveis.', aberta: false },
    { pergunta: 'Como funciona o pagamento?', resposta: 'O pagamento pode ser mensal ou por projeto, via pix, boleto ou cartão, conforme acordado em contrato.', aberta: false },
    { pergunta: 'Posso cancelar quando quiser?', resposta: 'Sim, nossos planos possuem cláusulas claras de cancelamento, que explicamos na proposta.', aberta: false },
    { pergunta: 'Vocês atendem em todo o Brasil?', resposta: 'Sim! Nosso atendimento é 100% online.', aberta: false },
  ];

  constructor(
    private cmsService: CMSService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadFaqs();
  }

  private loadFaqs(): void {
    this.cmsService.getCollection<{ question: string; answer: string }>('faq')
      .subscribe({
        next: data => {
          this.faqs = data?.length >= 1
            ? data.map((item, index) => ({
              pergunta: item.question,
              resposta: item.answer,
              aberta: index === 0,
            }))
            : this.fallbackFaqs;

          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.faqs = this.fallbackFaqs;
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
  }

  toggle(index: number): void {
    this.faqs.forEach((faq, i) => {
      faq.aberta = i === index ? !faq.aberta : false;
    });
  }
}